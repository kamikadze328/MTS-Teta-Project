package com.kamikadze328.mtstetaproject.presentation.moviedetails

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.moviedetailsactor.ActorAdapter
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.databinding.FragmentMovieDetailsBinding
import com.kamikadze328.mtstetaproject.presentation.State
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import com.kamikadze328.mtstetaproject.setRating
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()

    private val args: MovieDetailsFragmentArgs by navArgs()

    companion object {

        @JvmStatic
        fun newInstance(movieId: Int, parentTag: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate moviedetails")
        viewModel.setMovieId(args.movieId)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        viewModel.movieState.observe(viewLifecycleOwner, {
            when (it) {
                is State.LoadingState -> updateUI(viewModel.loadMovieLoading())
                is State.ErrorState -> updateUI(viewModel.loadMovieError())
                is State.DataState -> updateUI(it.data)
            }
        })

        setupRecyclerAdapters()

        prepareToolBar()
        return binding.root
    }

    private fun setupRecyclerAdapters() {
        setupRecyclerAdapterActors()
        setupRecyclerAdapterGenres()
    }

    private fun updateUI(movie: Movie) {
        binding.moviePoster.load(movie.poster_path)

        binding.movieDateText.text = movie.release_date
        binding.movieNameText.text = movie.title
        binding.movieNameTextToolbar.text = movie.title
        binding.movieDescription.text = movie.overview
        binding.movieAgeRestrictionText.text = movie.ageRestriction

        binding.movieRatingBarRootInclude.ratingBarRoot.setRating(movie.vote_average)
    }


    private fun setupRecyclerAdapterGenres() {
        val recyclerGenres = binding.movieGenresRecycler
        val adapter = GenreAdapter(::onClickListenerGenres)

        viewModel.genresState.observe(viewLifecycleOwner, {
            when (it) {
                is State.LoadingState -> adapter.submitList(viewModel.loadGenreLoading())
                is State.ErrorState -> adapter.submitList(viewModel.loadGenreError())
                is State.DataState -> adapter.submitList(it.data)
            }
        })

        recyclerGenres.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, 0, 0)
        recyclerGenres.addItemDecoration(itemDecorator)
    }

    private fun onClickListenerGenres(genreId: Int) {
        (activity as MainActivity).onGenreClicked(genreId)
    }

    private fun setupRecyclerAdapterActors() {
        val recyclerActors = binding.movieActorsRecycler
        val adapter = ActorAdapter()

        viewModel.actorsState.observe(viewLifecycleOwner, {
            when (it) {
                is State.LoadingState -> adapter.submitList(viewModel.loadActorsLoading())
                is State.ErrorState -> adapter.submitList(viewModel.loadActorsError())
                is State.DataState -> adapter.submitList(it.data)
            }
        })

        recyclerActors.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_detail_actors_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, 0, 0)
        recyclerActors.addItemDecoration(itemDecorator)

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
            scrollRange = binding.movieAppBar?.totalScrollRange ?: 0
        }

        binding.movieAppBar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
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


}