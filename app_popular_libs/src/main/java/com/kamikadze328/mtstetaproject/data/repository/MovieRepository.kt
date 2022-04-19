package com.kamikadze328.mtstetaproject.data.repository

import android.app.Application
import android.util.Log
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.local.dao.movie.MovieDao
import com.kamikadze328.mtstetaproject.data.mapper.toUIMovie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val webservice: Webservice,
    application: Application,
    private val movieDao: MovieDao,
    private val genreRepository: GenreRepository
) {
    private val movieDetailsError: Movie by lazy {
        Movie(
            movieId = -1,
            title = application.resources.getString(R.string.movie_loading_error),
            overview = application.resources.getString(R.string.movie_description_loading_error),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            age_restriction = application.resources.getString(R.string.age_restricting_loading),
            poster_path = ""
        )
    }

    private val movieDetailsLoading: Movie by lazy {
        Movie(
            movieId = -2,
            title = application.resources.getString(R.string.movie_name_loading),
            overview = application.resources.getString(R.string.movie_description_loading),
            release_date = application.resources.getString(R.string.movie_date_loading),
            vote_average = 0.0,
            age_restriction = application.resources.getString(R.string.age_restricting_loading),
            poster_path = ""
        )
    }

    suspend fun refreshPopularMovies(page: Int = 1): List<Movie> = withContext(Dispatchers.IO) {
        val allGenres = genreRepository.getAll()
        Log.d("kek", "refreshPopularMovies - $page")
        val movies = webservice.getMoviesPopular(page).results.map { it.toUIMovie(allGenres) }

        addAllLocal(movies)
        Log.d("kek", "refreshPopularMovies end - $movies")
        return@withContext movies
    }

    fun addLocal(movie: Movie): Long {
        return movieDao.insert(movie)
    }

    fun addAllLocal(movies: List<Movie>): List<Long> {
        return movieDao.insertAll(movies)
    }

    fun addLocalWithTime(movie: Movie): Long {
        return movieDao.insertWithTime(movie)
    }

    fun hasMovieLocal(movieId: Long): Boolean {
        return movieDao.hasMovie(movieId, FRESH_TIMEOUT)
    }

    fun setAllNotFavourite() {
        movieDao.changeMoviesFavourite()
    }

    fun changeMovieFavourite(movieId: Long, isFavourite: Boolean) {
        movieDao.changeMovieFavourite(movieId, isFavourite)
    }

    fun changeMoviesFavourite(movies: List<Movie>, isFavourite: Boolean) {
        movieDao.changeMoviesFavourite(movies.map { it.movieId }, isFavourite)
    }

    fun getAllFavourite() = movieDao.getAllFavourite()

    fun getMovieWithDetails(movieId: Long): Movie? {
        return movieDao.findMovieWithGenresAndActorsById(movieId)
    }

    fun getMovieLoading(): Movie {
        return movieDetailsLoading
    }

    fun getMovieError(): Movie {
        return movieDetailsError
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
        val movies = mutableListOf(
            Movie(
                movieId = 634649,
                title = "Spider-Man: No Way Home",
                overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
                release_date = "2021-12-15",
                vote_average = 8.2,
                age_restriction = "6+",
                poster_path = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 675353,
                title = "Sonic the Hedgehog 2",
                overview = "After settling in Green Hills, Sonic is eager to prove he has what it takes to be a true hero. His test comes when Dr. Robotnik returns, this time with a new partner, Knuckles, in search for an emerald that has the power to destroy civilizations. Sonic teams up with his own sidekick, Tails, and together they embark on a globe-trotting journey to find the emerald before it falls into the wrong hands.",
                release_date = "2022-03-30",
                vote_average = 7.7,
                age_restriction = "6+",
                poster_path = "=/6DrHO1jr3qVrViUO6s6kFiAGM7.jpg",
                isFavourite = false, updateTime = 1650393595771
            ),
            Movie(
                movieId = 508947,
                title = "Turning Red ",
                overview = "Thirteen-year-old Mei is experiencing the awkwardness of being a teenager with a twist – when she gets too excited, she transforms into a giant red panda.",
                release_date = "2022-03-10",
                vote_average = 7.5,
                age_restriction = "6+",
                poster_path = "=/qsdjk9oAKSQMWs0Vt5Pyfh6O4GZ.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 919689,
                title = "War of the Worlds : Annihilation ",
                overview = "A mother and son find themselves faced with a brutal alien invasion where survival will depend on discovering the unthinkable truth about the enemy.",
                release_date = "2021-12-22",
                vote_average = 6.7,
                age_restriction = "6+",
                poster_path = "=/9eiUNsUAw2iwVyMeXNNiNQQad4E.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 414906,
                title = "The Batman ",
                overview = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",
                release_date = "2022-03-01",
                vote_average = 7.9,
                age_restriction = "6+",
                poster_path = "=/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 406759,
                title = "Moonfall",
                overview = "A mysterious force knocks the moon from its orbit around Earth and sends it hurtling on a collision course with life as we know it.",
                release_date = "2022-02-03",
                vote_average = 6.5,
                age_restriction = "6+",
                poster_path = "=/odVv1sqVs0KxBXiA8bhIBlPgalx.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 696806,
                title = "The Adam Project",
                overview = "After accidentally crash-landing in 2022, time-traveling fighter pilot Adam Reed teams up with his 12-year-old self on a mission to save the future.",
                release_date = "2022-03-11",
                vote_average = 7.0,
                age_restriction = "6+",
                poster_path = "=/wFjboE0aFZNbVOF05fzrka9Fqyx.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 568124,
                title = "Encanto",
                overview = "The tale of an extraordinary family, the Madrigals, who live hidden in the mountains of Colombia, in a magical house, in a vibrant town, in a wondrous, charmed place called an Encanto. The magic of the Encanto has blessed every child in the family with a unique gift from super strength to the power to heal—every child except one, Mirabel. But when she discovers that the magic surrounding the Encanto is in danger, Mirabel decides that she, the only ordinary Madrigal, might just be her exceptional family's last hope.",
                release_date = "2021-11-24",
                vote_average = 7.7,
                age_restriction = "6+",
                poster_path = "=/4j0PNHkMr5ax3IA8tjtxcmPU3QT.jpg",
                isFavourite = false,
                updateTime = 1650393595771
            ),
            Movie(
                movieId = 823625,
                title = "Blacklight",
                overview = "Travis Block is a shadowy Government agent who specializes in removing operatives whose covers have been exposed. He then has to uncover a deadly conspiracy within his own ranks that reaches the highest echelons of power.",
                release_date = "2022-02-10",
                vote_average = 6.0,
                age_restriction = "6+",
                poster_path = "=/7gFo1PEbe1CoSgNTnjCGdZbw0zP.jpg",
                isFavourite = false,
                updateTime = 165
            ),
        )
    }
}