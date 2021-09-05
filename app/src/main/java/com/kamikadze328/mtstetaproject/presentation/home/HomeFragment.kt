package com.kamikadze328.mtstetaproject.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieItemDecoration
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.databinding.FragmentHomeBinding
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    lateinit var genreAdapter: GenreAdapter

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.home_exit_transition)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.movieMainMoviesRecycler.post { startPostponedEnterTransition() }

        setupRecyclerAdapters()

        setupSwipeRefresh()

        setupStringSearch()
    }

    private fun setupStringSearch() {
        binding.homeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setNewTextFilter(newText ?: "")
                return true
            }
        })
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

    private fun loadMore() {
        viewModel.loadMoreMovies()
    }

    private fun setupRecyclerAdapterMovies() {
        val recyclerMovies = binding.movieMainMoviesRecycler
        val adapter = MovieAdapter(::onClickListenerMovies, ::loadMore)

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
                is UIState.LoadingState -> {
                    binding.movieMainSwiperefresh.isRefreshing = true
                }
                is UIState.ErrorState -> {
                    adapter.submitList(emptyList())
                    binding.movieMainSwiperefresh.isRefreshing = false
                }
                is UIState.DataState -> {
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

        recyclerMovies.itemAnimator = SlideInUpAnimator(LinearOutSlowInInterpolator())

        viewModel.thereAreMoreMovies.observe(viewLifecycleOwner, {
            adapter.isLoadMore = it
        })
    }


    private fun setupRecyclerAdapterGenres() {
        val recyclerGenres = binding.movieMainGenresRecycler
        genreAdapter = GenreAdapter(::onClickListenerGenres)

        viewModel.genresState.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.LoadingState -> genreAdapter.submitList(viewModel.loadGenreLoading())
                is UIState.ErrorState -> genreAdapter.submitList(viewModel.loadGenreError())
                is UIState.DataState -> genreAdapter.submitList(it.data)
                else -> throw IllegalStateException()
            }
        })

        recyclerGenres.adapter = genreAdapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.movie_main_movie_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        recyclerGenres.addItemDecoration(itemDecorator)

        recyclerGenres.itemAnimator = SlideInRightAnimator(FastOutSlowInInterpolator())
    }

    private fun onClickListenerMovies(movieId: Long, view: View) {
        if (movieId <= 0) return
        val poster = view.findViewById<ImageView>(R.id.movie_main_poster)
        val actions =
            HomeFragmentDirections.actionHomeToMovieDetails(movieId, poster.transitionName)

        val extras = FragmentNavigatorExtras(poster to poster.transitionName)
        findNavController().navigate(actions, extras)
        //(activity as MainActivity).onMovieClicked(movieId)
    }

    private fun onClickListenerGenres(genreId: Long) {
        if (genreId <= 0) return
        (activity as MainActivity).onGenreClicked(genreId)
        viewModel.updateGenresFilter(genreId)
        val i = genreAdapter.currentList.indexOfFirst { it.genreId == genreId }
        genreAdapter.currentList.getOrNull(i)?.let {
            it.isSelected = !it.isSelected
        }
        genreAdapter.notifyItemChanged(i)
    }
}