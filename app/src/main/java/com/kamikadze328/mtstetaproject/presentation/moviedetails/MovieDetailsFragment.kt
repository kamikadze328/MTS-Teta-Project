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
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.moviedetailsactor.ActorAdapter
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.databinding.FragmentMovieDetailsBinding
import com.kamikadze328.mtstetaproject.setRating
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()


    companion object {
        const val PARENT_ID_ARG = "parent_id_arg"

        @JvmStatic
        fun newInstance(movieId: Int, parentTag: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(MovieDetailsViewModel.MOVIE_ID_ARG, movieId)
                    putString(PARENT_ID_ARG, parentTag)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate moviedetails")
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.setMovieId(it.getInt(MovieDetailsViewModel.MOVIE_ID_ARG))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        Log.d("kek", "onCreateView moviedetails")

        viewModel.movie.observe(viewLifecycleOwner, {
            updateUI(it)
        })


        prepareRecycleViewActors()
        prepareRecycleViewGenres()

        prepareToolBar()
        return binding.root
    }

    private fun updateUI(movie: Movie) {
        binding.moviePoster.load(movie.poster_path)

        binding.movieDateText.text = movie.release_date
        binding.movieNameText.text = movie.title
        binding.movieNameTextToolbar.text = movie.title
        binding.movieDescription.text = movie.overview
        binding.movieAgeRestrictionText.text =
            getString(R.string.main_age_restriction_text, movie.ageRestriction)

        binding.ratingBarRootInclude.ratingBarRoot.setRating(movie.vote_average)

    }


    private fun prepareRecycleViewGenres() {
        val recyclerGenres = binding.movieGenresRecycler
        val adapter = GenreAdapter(::onClickListenerGenres)

        viewModel.genres.observe(viewLifecycleOwner, {
            adapter.notifyItemRangeRemoved(0, 1)
            adapter.notifyItemRangeInserted(0, it.size)
            adapter.submitList(it)
        })

        recyclerGenres.adapter = adapter

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, 0, 0)
        recyclerGenres.addItemDecoration(itemDecorator)


        //recyclerGenres.scrollToPosition(recyclerGenresPosition)

    }

    private fun onClickListenerGenres(id: Int) {}

    private fun prepareRecycleViewActors() {
        val recyclerActors = binding.recycleViewActors
        val adapter = ActorAdapter()

        viewModel.actors.observe(viewLifecycleOwner, {
            adapter.submitList(it)
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