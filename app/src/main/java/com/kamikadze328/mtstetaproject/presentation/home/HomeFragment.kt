package com.kamikadze328.mtstetaproject.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieItemDecoration
import com.kamikadze328.mtstetaproject.databinding.FragmentHomeBinding
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var recyclerMoviesPosition = 0


    companion object {
        private const val RECYCLER_MOVIES_POSITION = "recycler_movies_position"

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("kek", "onCreate home")

        if (savedInstanceState != null) {
            Log.d("kek", "onCreate home saved instance")

            recyclerMoviesPosition = savedInstanceState.getInt(RECYCLER_MOVIES_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("kek", "onCreateView home")

        setupRecyclerAdapters()

        setupSwipeRefresh()

        return binding.root
    }

    private fun setupSwipeRefresh() {
        binding.movieMainSwiperefresh.isRefreshing = true
        binding.movieMainSwiperefresh.setOnRefreshListener(::onRefreshMovies)
    }

    private fun onRefreshMovies() {
        viewModel.loadMovies()
    }

    private fun setupRecyclerAdapters() {
        setupRecyclerAdapterMovies()
        setupRecyclerAdapterGenres()
    }

    private fun setupRecyclerAdapterMovies() {
        val recyclerMovies = binding.movieMainMoviesRecycler
        val adapter =
            MovieAdapter(::onClickListenerMovies/*, getString(R.string.movie_main_header_popular)*/)

        viewModel.movies.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            binding.movieMainSwiperefresh.isRefreshing = false
        })
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

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                Log.d("kek", "onItemRangeChanged")
                super.onItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                layoutManager.scrollToPosition(0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                layoutManager.scrollToPosition(0)
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            }
        })

        val itemDecorator = MovieItemDecoration(offsetBetween.toInt(), offsetBottom, spanCount)
        recyclerMovies.addItemDecoration(itemDecorator)

        recyclerMovies.scrollToPosition(recyclerMoviesPosition)
    }


    private fun setupRecyclerAdapterGenres() {
        val recyclerGenres = binding.movieMainGenresRecycler
        val adapter = GenreAdapter(::onClickListenerGenres)

        listOf(viewModel.getLoadingGenre(resources)).let {
            adapter.submitList(it)
        }

        viewModel.genres.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        recyclerGenres.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.movie_main_movie_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        recyclerGenres.addItemDecoration(itemDecorator)

    }

    private fun onClickListenerMovies(id: Int) {
        (activity as MainActivity).onMovieClicked(id)
    }

    /*private fun openMovieDetailsFragment() {
        val action =
            HomeFragmentDirections.actionNavigationHomeToMovieDetailsFragment(movieDetailsId)
        findNavController().navigate(action)
        isMovieDetailsOpened = true
    }*/

    private fun onClickListenerGenres(genreId: Int) {
        (activity as MainActivity).onGenreClicked(genreId)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("kek", "onSaveInstanceState")
        // Save state
        //Parcelable recyclerViewState
        /*val recyclerViewState = binding.movieMainMoviesRecycler.layoutManager?.onSaveInstanceState()
        savedInstanceState.putParcelable(RECYCLER_MOVIES_POSITION, recyclerViewState)*/
        /*
        // Restore state
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);*/

        val moviesPosition =
            (binding.movieMainMoviesRecycler.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

        savedInstanceState.putInt(RECYCLER_MOVIES_POSITION, moviesPosition)

        super.onSaveInstanceState(savedInstanceState)
    }
}