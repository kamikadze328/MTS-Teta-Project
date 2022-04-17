package com.kamikadze328.mtstetaproject.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kamikadze328.mtstetaproject.R


const val STARTS_COUNT = 5
const val FILLED_STAR = R.drawable.ic_star_filled
const val EMPTY_STAR = R.drawable.ic_star

@Composable
fun Rating(
    modifier: Modifier = Modifier,
    value: Double
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        var currentRating = 0.5
        var isRatingBelow = false
        repeat(STARTS_COUNT) {
            val isEmptyStar = isRatingBelow || value < currentRating++
            isRatingBelow = isEmptyStar
            val star = if (isEmptyStar) EMPTY_STAR else FILLED_STAR
            val starDescription =
                if (isEmptyStar) R.string.star_empty_description else R.string.star_filled_description
            Image(
                painter = painterResource(star),
                contentDescription = stringResource(starDescription),
            )
        }
    }
}