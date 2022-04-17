package com.kamikadze328.mtstetaproject.ui.movie.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.presentation.main.CallbackGenreClicked
import com.kamikadze328.mtstetaproject.ui.common.AgeRestriction
import com.kamikadze328.mtstetaproject.ui.common.ListGenre
import com.kamikadze328.mtstetaproject.ui.theme.background
import com.kamikadze328.mtstetaproject.ui.theme.background_transparent

@Composable
fun MovieDetailsHeader(
    modifier: Modifier = Modifier,
    movie: Movie,
    genres: List<Genre>,
    callback: CallbackGenreClicked,
) {
    val basePath =
        if (movie.movieId >= 0) Webservice.BASE_PATH_IMAGE_URL else Webservice.BASE_PATH_IMAGE_SMALL_URL
    val backgroundPath = Webservice.BASE_PATH_IMAGE_SMALL_URL + movie.poster_path
    val path = basePath + movie.poster_path
    Box(
        modifier = Modifier
            .height(dimensionResource(R.dimen.movie_detail_poster_height))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(backgroundPath)
                .build(),
            modifier = Modifier
                .fillMaxSize()
                .blur(70.dp),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .build(),
            modifier = Modifier
                .align(Alignment.Center),
            contentDescription = movie.title,
            contentScale = ContentScale.Fit,
        )

        HeaderDetails(
            modifier = modifier
                .height(105.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            movie = movie, genres = genres, callback = callback
        )
    }
}

@Composable
private fun HeaderDetails(
    modifier: Modifier = Modifier,
    movie: Movie,
    genres: List<Genre>,
    callback: CallbackGenreClicked
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        background_transparent,
                        background,
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp, top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ListGenre(genres = genres.subList(0, 1), callback = callback)
                    ReleaseDate(movie.release_date)
                }
                Title(movie.title)
            }
            AgeRestriction(value = movie.age_restriction, modifier = Modifier)
        }
    }
}

@Composable
private fun Title(
    value: String
) {
    Text(
        modifier = Modifier.padding(top = 14.dp),
        text = value,
        maxLines = 1,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun ReleaseDate(
    value: String
) {
    Text(
        text = value,
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 3.dp),
        fontSize = 12.sp,
    )
}