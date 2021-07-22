package com.kamikadze328.mtstetaproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamikadze328.mtstetaproject.MainActivity
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieItemDecoration
import com.kamikadze328.mtstetaproject.databinding.FragmentHomeBinding
import com.kamikadze328.mtstetaproject.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private var isMovieDetailsOpened = false
    private var movieDetailsId = 0
    private var recyclerMoviesPosition = 0
    private var recyclerGenresPosition = 0


    companion object {
        private const val RECYCLER_MOVIES_POSITION = "recycler_movies_position"
        private const val RECYCLER_GENRES_POSITION = "recycler_genres_position"
        private const val IS_MOVIE_DETAILS_OPENED = "is_movie_details_opened"
        private const val MOVIE_DETAILS_ID = "movie_details_id"


        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("kek", "onCreate home")

        if (savedInstanceState != null) {
            Log.d("kek", "onCreate home saved instance")

            recyclerMoviesPosition = savedInstanceState.getInt(RECYCLER_MOVIES_POSITION)
            recyclerGenresPosition = savedInstanceState.getInt(RECYCLER_GENRES_POSITION)
            isMovieDetailsOpened = savedInstanceState.getBoolean(IS_MOVIE_DETAILS_OPENED)
            movieDetailsId = savedInstanceState.getInt(MOVIE_DETAILS_ID)
            /*if(isMovieDetailsOpened)
                openMovieDetailsFragment()*/
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("kek", "onCreateView home")
        setupAdapters()
        return binding.root
    }

    private fun setupAdapters() {
        setupMoviesAdapter()
        setupGenresAdapter()
    }

    private fun setupMoviesAdapter() {
        val recyclerMovies = binding.movieMainRecycleView
        val adapter =
            MovieAdapter(::onClickListenerMovies, getString(R.string.movie_main_header_popular))


        viewModel.filteredMovies.observe(viewLifecycleOwner, {
            adapter.submitList(it)
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
                0, adapter.itemCount - 1 -> spanCount
                else -> 1
            }
        }
        recyclerMovies.layoutManager = layoutManager


        val itemDecorator = MovieItemDecoration(offsetBetween.toInt(), offsetBottom, spanCount)
        recyclerMovies.addItemDecoration(itemDecorator)

        recyclerMovies.scrollToPosition(recyclerMoviesPosition)
    }


    private fun setupGenresAdapter() {
        val recyclerGenres = binding.movieMainGenres

        val adapter = GenreAdapter(::onClickListenerGenres)

        viewModel.genres.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        recyclerGenres.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.movie_main_movie_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        recyclerGenres.addItemDecoration(itemDecorator)


        recyclerGenres.scrollToPosition(recyclerGenresPosition)
    }

    private fun onClickListenerMovies(id: Int) {
        movieDetailsId = id

        (activity as MainActivity).onMovieClicked(id)
        //openMovieDetailsFragment()
    }

    private fun openMovieDetailsFragment() {
        /*val action =
            HomeFragmentDirections.actionNavigationHomeToMovieDetailsFragment(movieDetailsId)
        findNavController().navigate(action)*/
        isMovieDetailsOpened = true
    }

    private fun onClickListenerGenres(id: Int) {
        Toast.makeText(context, "genre id - $id", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("kek", "onSaveInstanceState")

        val moviesPosition =
            (binding.movieMainRecycleView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        //val moviesPosition = recyclerMovies.computeVerticalScrollExtent()
        val genresPosition =
            (binding.movieMainGenres.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        savedInstanceState.putInt(RECYCLER_MOVIES_POSITION, moviesPosition)
        savedInstanceState.putInt(RECYCLER_GENRES_POSITION, genresPosition)
        savedInstanceState.putBoolean(IS_MOVIE_DETAILS_OPENED, isMovieDetailsOpened)
        savedInstanceState.putInt(MOVIE_DETAILS_ID, movieDetailsId)

        super.onSaveInstanceState(savedInstanceState)
    }
}