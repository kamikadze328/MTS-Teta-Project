package com.kamikadze328.mtstetaproject

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.kamikadze328.mtstetaproject.objects.Actor


class MainActivity : AppCompatActivity() {
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

    private fun prepareActors(): List<Actor> {
        return listOf(
            Actor(
                getActorDrawableIcon(R.drawable.img_actor_statham),
                "Джейсон Стэйтем"
            ),
            Actor(
                getActorDrawableIcon(R.drawable.img_actor_mccallany),
                "Холт Маккэллани"
            ),
            Actor(
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

    }
}