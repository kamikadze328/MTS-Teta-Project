package com.kamikadze328.mtstetaproject.data.repository


import android.app.Application
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.data.remote.dao.MovieShortRemote
import com.kamikadze328.mtstetaproject.data.util.SharedPrefsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountRepository @Inject constructor(
    private val webservice: Webservice,
    private val application: Application,
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository
) {
    val currentUser: FirebaseUser?
        get() = Firebase.auth.currentUser

    private val userError: User by lazy {
        User(
            id = "-1", name = application.resources.getString(R.string.user_loading_error),
            password = "", email = "", phone = "", photoUrl = Uri.EMPTY, emailVerified = true
        )
    }

    private val userLoading: User by lazy {
        User(
            id = "-2", name = application.resources.getString(R.string.user_loading),
            password = "", email = "", phone = "", photoUrl = Uri.EMPTY, emailVerified = true
        )
    }

    //TODO update and refresh user with FirebaseUser
    /*suspend fun refreshUser(accountId: Int): User = withContext(Dispatchers.IO) {
        return@withContext webservice.getAccountDetails(accountId.toString())
    }*/

    suspend fun getFavouriteGenres(accountId: String): List<Genre> = withContext(Dispatchers.IO) {
        val allGenres = genreRepository.getAll()
        val maxGenresCount = 3
        val favouriteMovies =
            getUserFavouriteMovies(accountId.toLong()).map { it.toUIMovie(allGenres) }

        movieRepository.changeMoviesFavourite(favouriteMovies, true)

        val countGenres = favouriteMovies.flatMap { it.genres }
            .groupingBy { it }.eachCount().toList()
            .sortedByDescending { (_, v) -> v }
            .subList(0, maxGenresCount - 1)

        val isMaxMoreOne = countGenres[0].second > 1
        return@withContext countGenres.filter { isMaxMoreOne && it.second > 1 }.map { it.first }
    }

    fun saveToken(token: String) {
        addSecurePrefsValue(THE_MOVIE_DB_TOKEN, token)
    }

    fun getToken(): String? {
        return getSecurePrefsValue(THE_MOVIE_DB_TOKEN)
    }

    private fun addSecurePrefsValue(key: String, value: String) {
        val prefs = SharedPrefsUtils.createSecure(application.applicationContext)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getSecurePrefsValue(key: String): String? {
        val prefs = SharedPrefsUtils.createSecure(application.applicationContext)
        return prefs.getString(key, null)
    }


    fun isLogin(): Boolean {
        return currentUser != null
    }

    fun logout() {
        Log.d("kek", "logout")
        movieRepository.setAllNotFavourite()
        Firebase.auth.signOut()
    }

    fun loadUserLoading(): User {
        return userLoading
    }

    fun loadUserError(): User {
        return userError
    }

    private fun getUserFavouriteMovies(uid: Long) = listOf(
        MovieShortRemote(
            id = 637649,
            title = "Гнев человеческий",
            overview = "Эйч — загадочный и холодный на вид джентльмен, но внутри него пылает жажда справедливости. Преследуя свои мотивы, он внедряется в инкассаторскую компанию, чтобы выйти на соучастников серии многомиллионных ограблений, потрясших Лос-Анджелес. В этой запутанной игре у каждого своя роль, но под подозрением оказываются все. Виновных же обязательно постигнет гнев человеческий.",
            vote_average = 3.9,
            adult = true,
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5JP9X5tCZ6qz7DYMabLmrQirlWh.jpg",
            release_date = "2021-07-23",
            genre_ids = listOf(28, 80),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 460465,
            title = "Мортал Комбат",
            overview = "Боец смешанных единоборств Коул Янг не раз соглашался проиграть за деньги. Он не знает о своем наследии и почему император Внешнего мира Шан Цзун посылает могущественного криомансера Саб-Зиро на охоту за Коулом. Янг боится за безопасность своей семьи, и майор спецназа Джакс, обладатель такой же отметки в виде дракона, как и у Коула, советует ему отправиться на поиски Сони Блейд. Вскоре Коул, Соня и наёмник Кано оказываются в храме Лорда Рейдена, Старшего Бога и защитника Земного царства, который дает убежище тем, кто носит метку. Здесь прибывшие тренируются с опытными воинами Лю Каном и Кун Лао, готовясь противостоять врагам из Внешнего мира, а для этого им нужно раскрыть в себе аркану — могущественную силу души.",
            vote_average = 3.75,
            adult = true,
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/pMIixvHwsD5RZxbvgsDSNkpKy0R.jpg",
            release_date = "2021-04-23",
            genre_ids = listOf(28, 12, 14),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 587562,
            title = "Упс... Приплыли!",
            overview = "От Великого потопа зверей спас ковчег. Но спустя полгода скитаний они готовы сбежать с него куда угодно. Нервы на пределе. Хищники готовы забыть про запреты и заглядываются на травоядных. Единственное спасение — найти райский остров. Там простор и полно еды. Но даже если он совсем близко, будут ли рады местные такому количеству гостей?",
            vote_average = 3.9,
            adult = false, //"6+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/546RNYy9Wi5wgboQ7EtD6i0DY5D.jpg",
            release_date = "2020-10-23",
            genre_ids = listOf(16, 12, 10751, 35),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 728814,
            title = "The Box",
            overview = "Уличный музыкант знакомится с музыкальным продюсером, и они вдвоём отправляются в путешествие, которое перевернёт их жизни.",
            vote_average = 3.0,
            adult = false,//"12+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/fq3DSw74fAodrbLiSv0BW1Ya4Ae.jpg",
            release_date = "2021-03-24",
            genre_ids = listOf(18, 10402),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 765057,
            title = "Сага о Дэнни Эрнандесе",
            overview = "Tekashi69 или Сикснайн — знаменитый бруклинский рэпер с радужным волосам —  прогремел синглом «Gummo», коллабом с Ники Минаж, а также многочисленными преступлениями. В документальном расследовании о жизни и творчестве рэпера разворачивается настоящая гангстерская история, в которой количество обвинений растет пропорционально интернет-популярности, а репутация секс-наркомана получает свое подтверждение не только в скандальных видео музыканта. Похоже, это последний герой нашего времени, которого не коснулась культура отмены: у Tekashi69 24 млн. подписчиков в Instagram, миллиард просмотров на Youtube и несколько судимостей.",
            vote_average = 2.5,
            adult = true, //"18+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5xXGQLVtTAExHY92DHD9ewGmKxf.jpg",
            release_date = "2021-04-29",
            genre_ids = listOf(10402, 99),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 681260,
            title = "Пчелка Майя",
            overview = "Когда упрямая пчелка Майя и ее лучший друг Вилли спасают принцессу-муравьишку, начинается сказочное и опасное приключение, которое приведет их в необычные новые миры и проверит их дружбу на прочность.",
            vote_average = 3.25,
            adult = false,// "0+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/xltjMeLlxywym14NEizl0metO10.jpg",
            release_date = "2021-05-17",
            genre_ids = listOf(16, 12, 10751),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 337404,
            title = "Круэлла",
            overview = "Невероятно одаренная мошенница по имени Эстелла решает сделать себе имя в мире моды. Её лучшие друзья — парочка юных карманников, которые ценят страсть Эстеллы к приключениям и надеются вместе с ней отвоевать себе место под солнцем на улицах британской столицы. В один прекрасный день модное чутье Эстеллы привлекает внимание шикарной и пугающе высокомерной баронессы фон Хельман.",
            vote_average = 4.2,
            adult = false, //"12+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hUfyYGP9Xf6cHF9y44JXJV3NxZM.jpg",
            release_date = "2021-05-28",
            genre_ids = listOf(35, 80),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        ),
        MovieShortRemote(
            id = 497698,
            title = "Чёрная вдова",
            overview = "Наташе Романофф предстоит лицом к лицу встретиться со своим прошлым. Чёрной Вдове придётся вспомнить о том, что было в её жизни задолго до присоединения к команде Мстителей, и узнать об опасном заговоре, в который оказываются втянуты её старые знакомые — Елена, Алексей (известный как Красный Страж) и Мелина.\n",
            vote_average = 4.0,
            adult = false,//"16+",
            poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/mbtN6V6y5kdawvAkzqN4ohi576a.jpg",
            release_date = "2021-05-28",
            genre_ids = listOf(28, 12, 53, 879),
            backdrop_path = "",
            original_language = "",
            original_title = "",
            popularity = .0,
            video = false,
            vote_count = 0
        )
    ).shuffled().subList(0, 5)

    companion object {
        const val THE_MOVIE_DB_TOKEN = "th_mv_db_tkn"

    }
}