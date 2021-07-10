package com.kamikadze328.mtstetaproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import com.google.android.material.appbar.AppBarLayout


class MainActivity : AppCompatActivity() {
    private var scrollRange: Int = 0
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarBeforeCollapsed: ConstraintLayout
    private var isCollapsed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)


        initToolBar()
    }


    private fun initToolBar() {
        toolbar = findViewById(R.id.toolbar)
        toolbarBeforeCollapsed = findViewById(R.id.toolbarBeforeCollapsed)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
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
}