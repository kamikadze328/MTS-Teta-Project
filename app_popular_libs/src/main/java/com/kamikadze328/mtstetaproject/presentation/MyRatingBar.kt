package com.kamikadze328.mtstetaproject

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat


fun ConstraintLayout.setRating(
    rating: Double,
    ids: IntArray = intArrayOf(
        R.id.movie_rating_star_1,
        R.id.movie_rating_star_2,
        R.id.movie_rating_star_3,
        R.id.movie_rating_star_4,
        R.id.movie_rating_star_5
    )
) {
    var currentRating = 0.5
    var isRatingBelow = false
    for (id in ids) {
        val star = findViewById<ImageView>(id)
        if (isRatingBelow || rating < currentRating++) {
            star.clearStar()
            isRatingBelow = true
        } else {
            star.fillStar()
        }
    }
}


fun ImageView.fillStar(drawableId: Int = R.drawable.ic_star_filled) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableId))
}

fun ImageView.clearStar(drawableId: Int = R.drawable.ic_star) {
    setImageDrawable(ContextCompat.getDrawable(context, drawableId))
}