package com.kamikadze328.mtstetaproject.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.remote.Webservice

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieShortGrid(
    movies: List<Movie>,
    onMovieClick: (id: Long) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
        items(movies) {
            MovieShort(it, onMovieClick)
        }
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun MovieShort(
    movie: Movie,
    onMovieClick: (id: Long) -> Unit,
) {
    val cornerRadius = with(LocalDensity.current) {
        dimensionResource(R.dimen.movie_main_poster_border_radius).toPx()
    }
    val url = Webservice.BASE_PATH_IMAGE_SMALL_URL + movie.poster_path
    val painter = rememberImagePainter(url) {
        placeholder(R.drawable.ic_baseline_image_not_supported_24)
        transformations(RoundedCornersTransformation(cornerRadius))
    }

    Card(
        onClick = { onMovieClick(movie.movieId) }
    ) {
        Column {
            Image(
                painter = painter,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.main_poster_width))
                    .height(dimensionResource(R.dimen.main_poster_height))
                    .align(Alignment.CenterHorizontally),
                contentDescription = movie.title,
                contentScale = ContentScale.Fit,
            )
            Text(
                movie.title,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                movie.overview ?: "",
                fontSize = 12.sp,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
            )
            Row {

            }
        }
    }
}
