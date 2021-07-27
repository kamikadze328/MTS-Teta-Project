package com.kamikadze328.mtstetaproject.data.network

import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.dto.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Webservice @Inject constructor() {
    suspend fun getAccountDetails(accountId: String): User {
        Thread.sleep(2000)
        return User(
            id = accountId.toInt(),
            name = "Банан$accountId",
            password = "12345678",
            email = "bananbananovich@gmail.com",
            phone = "+79999999999"
        )
    }


    suspend fun getUserFavouriteMovies(accountId: String): List<Movie> {
        Thread.sleep(2000)
        return getMoviesByCategory("popular").shuffled().subList(0, 5)
    }

    suspend fun getActorsByMovieId(movieId: String): List<Actor> {
        Thread.sleep(2000)
        return listOf(
            Actor(
                R.drawable.img_actor_statham,
                "Джейсон Стэйтем"
            ),
            Actor(
                R.drawable.img_actor_mccallany,
                "Холт Маккэллани"
            ),
            Actor(
                R.drawable.img_actor_hartnett,
                "Джош Харнетт"
            )
        )
    }

    suspend fun getGenres(): List<Genre> {
        Thread.sleep(2000)

        return listOf(
            Genre(
                name = "Боевик",
                id = 28
            ),
            Genre(
                name = "Приключения",
                id = 12
            ),
            Genre(
                name = "Мультик",
                id = 16
            ),
            Genre(
                name = "Комедия",
                id = 35
            ),
            Genre(
                name = "Криминал",
                id = 80
            ),
            Genre(
                name = "Документальное",
                id = 99
            ),
            Genre(
                name = "Драма",
                id = 18
            ),
            Genre(
                name = "Семейный",
                id = 10751
            ),
            Genre(
                name = "Фантастика",
                id = 14
            ),
            Genre(
                name = "Исторический",
                id = 36
            ),
            Genre(
                name = "Ужасы",
                id = 27
            ),
            Genre(
                name = "Музыка",
                id = 10402
            ),
            Genre(
                name = "Мистика",
                id = 9648
            ),
            Genre(
                name = "Романтика",
                id = 10749
            ),
            Genre(
                name = "Научная фантастика",
                id = 879
            ),
            Genre(
                name = "Триллер",
                id = 53
            ),
            Genre(
                name = "Военный",
                id = 10752
            ),
            Genre(
                name = "Вестерн",
                id = 37
            ),
            Genre(
                name = "Рельное ТВ",
                id = 10770
            ),
        )
    }

    suspend fun getGenresById(genreId: String): Genre? {
        Thread.sleep(2000)
        val id = genreId.toInt()
        return getGenres().find { it.id == id }
    }

    suspend fun getPopularMovies() = getMoviesByCategory("popular")

    suspend fun getMoviesByCategory(category: String): List<Movie> {
        Thread.sleep(2000)
        return listOf(
            Movie(
                id = 637649,
                title = "Гнев человеческий",
                overview = "Эйч — загадочный и холодный на вид джентльмен, но внутри него пылает жажда справедливости. Преследуя свои мотивы, он внедряется в инкассаторскую компанию, чтобы выйти на соучастников серии многомиллионных ограблений, потрясших Лос-Анджелес. В этой запутанной игре у каждого своя роль, но под подозрением оказываются все. Виновных же обязательно постигнет гнев человеческий.",
                vote_average = 3.9,
                ageRestriction = 18,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5JP9X5tCZ6qz7DYMabLmrQirlWh.jpg",
                genre_ids = listOf(28, 80),
                release_date = "2021-07-23"
            ),
            Movie(
                id = 460465,
                title = "Мортал Комбат",
                overview = "Боец смешанных единоборств Коул Янг не раз соглашался проиграть за деньги. Он не знает о своем наследии и почему император Внешнего мира Шан Цзун посылает могущественного криомансера Саб-Зиро на охоту за Коулом. Янг боится за безопасность своей семьи, и майор спецназа Джакс, обладатель такой же отметки в виде дракона, как и у Коула, советует ему отправиться на поиски Сони Блейд. Вскоре Коул, Соня и наёмник Кано оказываются в храме Лорда Рейдена, Старшего Бога и защитника Земного царства, который дает убежище тем, кто носит метку. Здесь прибывшие тренируются с опытными воинами Лю Каном и Кун Лао, готовясь противостоять врагам из Внешнего мира, а для этого им нужно раскрыть в себе аркану — могущественную силу души.",
                vote_average = 3.75,
                ageRestriction = 18,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/pMIixvHwsD5RZxbvgsDSNkpKy0R.jpg",
                genre_ids = listOf(28, 12, 14),
                release_date = "2021-04-23"
            ),
            Movie(
                id = 587562,
                title = "Упс... Приплыли!",
                overview = "От Великого потопа зверей спас ковчег. Но спустя полгода скитаний они готовы сбежать с него куда угодно. Нервы на пределе. Хищники готовы забыть про запреты и заглядываются на травоядных. Единственное спасение — найти райский остров. Там простор и полно еды. Но даже если он совсем близко, будут ли рады местные такому количеству гостей?",
                vote_average = 3.9,
                ageRestriction = 6,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/546RNYy9Wi5wgboQ7EtD6i0DY5D.jpg",
                genre_ids = listOf(16, 12, 10751, 35),
                release_date = "2020-10-23"
            ),
            Movie(
                id = 728814,
                title = "The Box",
                overview = "Уличный музыкант знакомится с музыкальным продюсером, и они вдвоём отправляются в путешествие, которое перевернёт их жизни.",
                vote_average = 3.0,
                ageRestriction = 12,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/fq3DSw74fAodrbLiSv0BW1Ya4Ae.jpg",
                genre_ids = listOf(18, 10402),
                release_date = "2021-03-24"
            ),
            Movie(
                id = 765057,
                title = "Сага о Дэнни Эрнандесе",
                overview = "Tekashi69 или Сикснайн — знаменитый бруклинский рэпер с радужным волосам —  прогремел синглом «Gummo», коллабом с Ники Минаж, а также многочисленными преступлениями. В документальном расследовании о жизни и творчестве рэпера разворачивается настоящая гангстерская история, в которой количество обвинений растет пропорционально интернет-популярности, а репутация секс-наркомана получает свое подтверждение не только в скандальных видео музыканта. Похоже, это последний герой нашего времени, которого не коснулась культура отмены: у Tekashi69 24 млн. подписчиков в Instagram, миллиард просмотров на Youtube и несколько судимостей.",
                vote_average = 2.5,
                ageRestriction = 18,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/5xXGQLVtTAExHY92DHD9ewGmKxf.jpg",
                genre_ids = listOf(10402, 99),
                release_date = "2021-04-29"
            ),
            Movie(
                id = 681260,
                title = "Пчелка Майя",
                overview = "Когда упрямая пчелка Майя и ее лучший друг Вилли спасают принцессу-муравьишку, начинается сказочное и опасное приключение, которое приведет их в необычные новые миры и проверит их дружбу на прочность.",
                vote_average = 3.25,
                ageRestriction = 0,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/xltjMeLlxywym14NEizl0metO10.jpg",
                genre_ids = listOf(16, 12, 10751),
                release_date = "2021-05-17"
            ),
            Movie(
                id = 337404,
                title = "Круэлла",
                overview = "Невероятно одаренная мошенница по имени Эстелла решает сделать себе имя в мире моды. Её лучшие друзья — парочка юных карманников, которые ценят страсть Эстеллы к приключениям и надеются вместе с ней отвоевать себе место под солнцем на улицах британской столицы. В один прекрасный день модное чутье Эстеллы привлекает внимание шикарной и пугающе высокомерной баронессы фон Хельман.",
                vote_average = 4.2,
                ageRestriction = 12,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/hUfyYGP9Xf6cHF9y44JXJV3NxZM.jpg",
                genre_ids = listOf(35, 80),
                release_date = "2021-05-28"
            ),
            Movie(
                id = 497698,
                title = "Чёрная вдова",
                overview = "Наташе Романофф предстоит лицом к лицу встретиться со своим прошлым. Чёрной Вдове придётся вспомнить о том, что было в её жизни задолго до присоединения к команде Мстителей, и узнать об опасном заговоре, в который оказываются втянуты её старые знакомые — Елена, Алексей (известный как Красный Страж) и Мелина.\n",
                vote_average = 4.0,
                ageRestriction = 16,
                poster_path = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/mbtN6V6y5kdawvAkzqN4ohi576a.jpg",
                genre_ids = listOf(20, 12, 53, 879),
                release_date = "2021-05-28"
            ),
        ).shuffled()
    }

    suspend fun getMovieById(movieId: String): Movie? {
        val id = movieId.toInt()
        return getPopularMovies().find { it.id == id }
    }

}