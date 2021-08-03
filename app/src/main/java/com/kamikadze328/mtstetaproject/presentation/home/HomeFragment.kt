package com.kamikadze328.mtstetaproject.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieItemDecoration
import com.kamikadze328.mtstetaproject.databinding.FragmentHomeBinding
import com.kamikadze328.mtstetaproject.presentation.State
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate home")
        Log.d("kek", "${findNavController().backQueue}")
        Log.d("kek", "to - ${findNavController().currentDestination}")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("kek", "onCreateView home")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerAdapters()

        setupSwipeRefresh()
        return binding.root
    }

    private fun setupSwipeRefresh() {
        binding.movieMainSwiperefresh.setOnRefreshListener(::onRefresh)
    }

    private fun onRefresh() {
        viewModel.clearRecyclerMoviesState()
        viewModel.loadAllData()
    }

    private fun setupRecyclerAdapters() {
        setupRecyclerAdapterMovies()
        setupRecyclerAdapterGenres()
    }

    private fun setupRecyclerAdapterMovies() {
        val recyclerMovies = binding.movieMainMoviesRecycler
        val adapter = MovieAdapter(::onClickListenerMovies)

        recyclerMovies.adapter = adapter

        val widthPoster = resources.getDimension(R.dimen.main_poster_width)
        val widthScreen = resources.displayMetrics.widthPixels

        val offsetBetween = resources.getDimension(R.dimen.movie_main_movie_offset)
        val offsetBottom = resources.getDimension(R.dimen.movie_main_movie_offset_bottom).toInt()

        val spanCount =
            if (widthScreen > offsetBetween * 4 + widthPoster * 3)
                if (widthScreen > offsetBetween * 5 + widthPoster * 4) 4 else 3
            else 2

        val layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when (position) {
                /*0, */adapter.itemCount - 1 -> spanCount
                else -> 1
            }
        }
        recyclerMovies.layoutManager = layoutManager

        viewModel.moviesState.observe(viewLifecycleOwner, {
            when (it) {
                is State.LoadingState -> {
                    binding.movieMainSwiperefresh.isRefreshing = true
                }
                is State.ErrorState -> {
                    adapter.submitList(emptyList())
                    binding.movieMainSwiperefresh.isRefreshing = false
                }
                is State.DataState -> {
                    adapter.submitList(it.data) {
                        if (viewModel.recyclerMoviesState.value == null)
                            recyclerMovies.scrollToPosition(0)
                        else layoutManager.onRestoreInstanceState(viewModel.recyclerMoviesState.value)
                    }
                    binding.movieMainSwiperefresh.isRefreshing = false
                }
            }
        })

        val itemDecorator = MovieItemDecoration(offsetBetween.toInt(), offsetBottom, spanCount)
        recyclerMovies.addItemDecoration(itemDecorator)

        recyclerMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    viewModel.setRecyclerMoviesState(layoutManager)
            }
        })

        layoutManager.onRestoreInstanceState(viewModel.recyclerMoviesState.value)
    }


    private fun setupRecyclerAdapterGenres() {
        val recyclerGenres = binding.movieMainGenresRecycler
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
        val firstLastOffset = resources.getDimension(R.dimen.movie_main_movie_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        recyclerGenres.addItemDecoration(itemDecorator)

    }

    private fun onClickListenerMovies(movieId: Int) {
        val actions = HomeFragmentDirections.actionHomeToMovieDetails(movieId)
        findNavController().navigate(actions)
        //(activity as MainActivity).onMovieClicked(movieId)
    }

    private fun onClickListenerGenres(genreId: Int) {
        (activity as MainActivity).onGenreClicked(genreId)
    }
}