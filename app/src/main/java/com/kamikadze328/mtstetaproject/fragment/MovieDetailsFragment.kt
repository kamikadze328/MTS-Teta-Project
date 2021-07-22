package com.kamikadze328.mtstetaproject.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.moviedetailsactor.ActorAdapter
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.features.actors.ActorsDataSourceImpl
import com.kamikadze328.mtstetaproject.data.features.genres.GenresDataSourceImpl
import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSourceImpl
import com.kamikadze328.mtstetaproject.databinding.FragmentMovieDetailsBinding
import com.kamikadze328.mtstetaproject.model.ActorsModel
import com.kamikadze328.mtstetaproject.model.GenresModel
import com.kamikadze328.mtstetaproject.model.MoviesModel
import com.kamikadze328.mtstetaproject.setRating


class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var actorsModel: ActorsModel
    private lateinit var moviesModel: MoviesModel
    private lateinit var genresModel: GenresModel

    private var movie: Movie? = null

    private var movieId = 0

    companion object {
        private const val MOVIE_ID_ARG = "movie_id_arg"
        const val PARENT_ID_ARG = "parent_id_arg"

        @JvmStatic
        fun newInstance(movieId: Int, parentTag: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(MOVIE_ID_ARG, movieId)
                    putString(PARENT_ID_ARG, parentTag)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataSource()
        arguments?.let {
            movieId = it.getInt(MOVIE_ID_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        movie = moviesModel.getMovieById(movieId)

        if (movie != null) {
            binding.movieNameText.text = movie!!.title
            binding.movieNameTextToolbar.text = movie!!.title
            binding.moviePoster.load(movie!!.poster_path)
            binding.movieDescription.text = movie!!.overview
            binding.movieAgeRestrictionText.text =
                getString(R.string.main_age_restriction_text, movie!!.ageRestriction)

            binding.ratingBarRootInclude.ratingBarRoot.setRating(movie!!.vote_average)
            prepareRecycleViewActors()
            prepareRecycleViewGenres()
        }

        prepareToolBar()
        return binding.root
    }


    private fun initDataSource() {
        actorsModel = ActorsModel(ActorsDataSourceImpl())
        moviesModel = MoviesModel(MoviesDataSourceImpl())
        genresModel = GenresModel(GenresDataSourceImpl())
    }

    private fun prepareRecycleViewGenres() {
        val recyclerGenres = binding.movieGenresRecycler

        val adapter = GenreAdapter(::onClickListenerGenres)

        val genres: List<Genre> = movie!!.genre_ids
            .filter { genresModel.getGenreById(it) != null }
            .map { genresModel.getGenreById(it)!! }

        adapter.submitList(genres)

        recyclerGenres.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, 0, 0)
        recyclerGenres.addItemDecoration(itemDecorator)


        //recyclerGenres.scrollToPosition(recyclerGenresPosition)

    }

    private fun onClickListenerGenres(id: Int) {}

    private fun prepareRecycleViewActors() {
        val genres = actorsModel.getActors()
        val adapter = ActorAdapter()

        adapter.submitList(genres)
        binding.recycleViewActors.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_detail_actors_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, 0, 0)
        binding.recycleViewActors.addItemDecoration(itemDecorator)

    }

    private fun prepareToolBar() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            prepareToolBarPortrait()
        else prepareToolBarLand()
    }

    private fun prepareToolBarPortrait() {
        var scrollRange = 0
        var isCollapsed = false

        binding.toolbar.doOnLayout {
            scrollRange = binding.appBarLayout?.totalScrollRange ?: 0
        }

        binding.appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (isCollapsed) {
                isCollapsed = false
                binding.toolbarBeforeCollapsed?.visibility = View.VISIBLE
                binding.toolbar.visibility = View.INVISIBLE
            } else if (scrollRange + verticalOffset == 0) {
                isCollapsed = true
                binding.toolbar.visibility = View.VISIBLE
                binding.toolbarBeforeCollapsed?.visibility = View.INVISIBLE
            }
        })
    }

    private fun prepareToolBarLand() {
        binding.movieContentScroll?.viewTreeObserver?.addOnScrollChangedListener {
            val location = IntArray(2)
            binding.movieNameText.getLocationOnScreen(location)
            val y = location[1]
            binding.toolbar.visibility =
                if (y - binding.movieNameText.lineHeight <= 0) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("kek", "onSaveInstanceState details")
        super.onSaveInstanceState(outState)
    }
}