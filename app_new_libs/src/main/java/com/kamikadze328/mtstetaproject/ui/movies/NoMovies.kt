package com.kamikadze328.mtstetaproject.ui.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.ui.common.MainHeader

@Composable
fun NoMovies(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        MainHeader(textRes = R.string.movie_main_no_movies_header)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.movie_main_no_movies_description),
                    textAlign = TextAlign.Center,
                )
                Image(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.movie_main_no_movies_img_size))
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.ic_baseline_videocam_off_24),
                    contentDescription = stringResource(R.string.movie_main_no_movies_header),
                )
            }
        }
    }
}