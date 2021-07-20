package com.kamikadze328.mtstetaproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.genre.GenreItemDecorator
import com.kamikadze328.mtstetaproject.adapter.movie.MovieAdapter
import com.kamikadze328.mtstetaproject.adapter.movie.MovieItemDecoration
import com.kamikadze328.mtstetaproject.data.features.genres.GenresDataSourceImpl
import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSourceImpl
import com.kamikadze328.mtstetaproject.model.GenresModel
import com.kamikadze328.mtstetaproject.model.MoviesModel


class MainActivity : AppCompatActivity() {
    companion object {
        private const val RECYCLER_MOVIES_POSITION = "recycler_movies_position"
        private const val RECYCLER_GENRES_POSITION = "recycler_genres_position"
    }

    private lateinit var moviesModel: MoviesModel
    private lateinit var genresModel: GenresModel
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var recyclerGenres: RecyclerView
    private var recyclerMoviesPosition: Int = 0
    private var recyclerGenresPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            recyclerMoviesPosition = savedInstanceState.getInt(RECYCLER_MOVIES_POSITION)
            recyclerGenresPosition = savedInstanceState.getInt(RECYCLER_GENRES_POSITION)
        }

        initDataSource()
        setupAdapters()

    }

    private fun setupAdapters() {
        setupMoviesAdapter()
        setupGenresAdapter()
    }

    private fun initDataSource() {
        moviesModel = MoviesModel(MoviesDataSourceImpl())
        genresModel = GenresModel(GenresDataSourceImpl())
    }

    private fun setupMoviesAdapter() {
        recyclerMovies = findViewById(R.id.movie_main_recycle_view)
        val movies = moviesModel.getMovies()
        val adapter =
            MovieAdapter(::onClickListenerMovies, getString(R.string.movie_main_header_popular))

        adapter.submitList(movies)
        recyclerMovies.adapter = adapter


        val widthPoster = resources.getDimension(R.dimen.main_poster_width)
        val widthScreen = resources.displayMetrics.widthPixels

        val offsetBetween = resources.getDimension(R.dimen.movie_main_movie_offset)
        val offsetBottom = resources.getDimension(R.dimen.movie_main_movie_offset_bottom).toInt()

        val spanCount =
            if (widthScreen > offsetBetween * 4 + widthPoster * 3)
                if (widthScreen > offsetBetween * 5 + widthPoster * 4) 4 else 3
            else 2

        val layoutManager = GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false)
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
        //recyclerMovies.scrollTo(0, recyclerMoviesPosition)
    }


    private fun setupGenresAdapter() {
        recyclerGenres = findViewById(R.id.movie_main_genres)
        val genres = genresModel.getGenres()
        val adapter = GenreAdapter(::onClickListenerGenres)

        adapter.submitList(genres)
        recyclerGenres.adapter = adapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerGenres.layoutManager = layoutManager

        val offset = resources.getDimension(R.dimen.movie_main_genres_offset).toInt()
        val firstOffset = resources.getDimension(R.dimen.movie_main_movie_offset).toInt()

        val itemDecorator = GenreItemDecorator(offset, firstOffset)
        recyclerGenres.addItemDecoration(itemDecorator)


        recyclerGenres.scrollToPosition(recyclerGenresPosition)
    }

    private fun onClickListenerMovies(title: String) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }

    private fun onClickListenerGenres(id: Int) {
        Toast.makeText(this, "genre id - $id", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        val moviesPosition =
            (recyclerMovies.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        //val moviesPosition = recyclerMovies.computeVerticalScrollExtent()
        val genresPosition =
            (recyclerGenres.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        savedInstanceState.putInt(RECYCLER_MOVIES_POSITION, moviesPosition)
        savedInstanceState.putInt(RECYCLER_GENRES_POSITION, genresPosition)

        super.onSaveInstanceState(savedInstanceState)
    }

}

/*
----------------------------------------
------- Activity Movie Details ---------
----------------------------------------

//TODO save scroll position
private lateinit var toolbar: Toolbar
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_details)


    prepareRecycleViewActors()
    prepareToolBar()

}
private fun prepareRecycleViewActors() {
    val recycler = findViewById<RecyclerView>(R.id.recycle_view_actors)
    val actors = prepareActors()
    val adapter = MovieActorsAdapter.ActorAdapter(actors)
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    val actorsDivider =
        ResourcesCompat.getDrawable(resources, R.drawable.movie_actors_divider, theme)!!
    val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
    dividerItemDecoration.setDrawable(actorsDivider)
    recycler.addItemDecoration(dividerItemDecoration)
}

private fun prepareActors(): List<ActorDto> {
    return listOf(
        ActorDto(
            getActorDrawableIcon(R.drawable.img_actor_statham),
            "Джейсон Стэйтем"
        ),
        ActorDto(
            getActorDrawableIcon(R.drawable.img_actor_mccallany),
            "Холт Маккэллани"
        ),
        ActorDto(
            getActorDrawableIcon(R.drawable.img_actor_hartnett),
            "Джош Харнетт"
        )
    )
}

private fun getActorDrawableIcon(id: Int) =
    ResourcesCompat.getDrawable(resources, id, theme) ?: ResourcesCompat.getDrawable(
        resources,
        R.drawable.ic_baseline_image_not_supported_24,
        theme
    )

private fun prepareToolBar(){
    toolbar = findViewById(R.id.toolbar)
    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        prepareToolBarPortrait()
    else prepareToolBarLand()
}
private fun prepareToolBarPortrait() {
    val toolbarBeforeCollapsed: ConstraintLayout = findViewById(R.id.toolbar_before_collapsed)
    var scrollRange = 0
    var isCollapsed = false

    val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)
    toolbar.doOnLayout {
        scrollRange = appBarLayout.totalScrollRange
    }

    appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->

        if (isCollapsed) {
            isCollapsed = false
            toolbarBeforeCollapsed.visibility = View.VISIBLE
            toolbar.visibility = View.INVISIBLE
        } else if (scrollRange + verticalOffset == 0) {
            isCollapsed = true
            toolbar.visibility = View.VISIBLE
            toolbarBeforeCollapsed.visibility = View.INVISIBLE
        }
    })
}

private fun prepareToolBarLand() {
    val movieNameTextView = findViewById<TextView>(R.id.movie_name_text)

    findViewById<ScrollView>(R.id.movie_content_scroll).viewTreeObserver.addOnScrollChangedListener {
        val location = IntArray(2)
        movieNameTextView.getLocationOnScreen(location)
        val y = location[1]
        toolbar.visibility =
            if (y - movieNameTextView.lineHeight <= 0) View.VISIBLE else View.INVISIBLE
    }

}*/
