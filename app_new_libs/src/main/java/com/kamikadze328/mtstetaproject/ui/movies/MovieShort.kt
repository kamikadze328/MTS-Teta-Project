package com.kamikadze328.mtstetaproject.ui.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Movie
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.presentation.main.CallbackMovieClicked

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieShortGrid(
    movies: List<Movie>,
    onMovieClick: CallbackMovieClicked
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 48.dp),
    ) {
        items(movies) {
            MovieShort(it, onMovieClick)
        }    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridScope.MovieShortGrid(
    movies: List<Movie>,
    onMovieClick: (id: Long) -> Unit
) {
    items(movies) {
        MovieShort(it, onMovieClick)
    }
}

@Composable
fun MovieShort(
    movie: Movie,
    callback: CallbackMovieClicked,
) {
    val cornerRadius = with(LocalDensity.current) {
        dimensionResource(R.dimen.movie_main_poster_border_radius).toPx()
    }
    val url = Webservice.BASE_PATH_IMAGE_SMALL_URL + movie.poster_path

    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { callback.onMovieClicked(movie.movieId) }
            )
            .padding(horizontal = 5.dp, vertical = 12.dp)
            .padding(3.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                .transformations(RoundedCornersTransformation(cornerRadius))
                .build(),
            modifier = Modifier
                .width(dimensionResource(R.dimen.main_poster_width))
                .height(dimensionResource(R.dimen.main_poster_height))
                .align(Alignment.CenterHorizontally),
            contentDescription = movie.title,
            contentScale = ContentScale.Fit,
        )
        Text(
            movie.title,
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            movie.overview ?: "",
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 12.sp,
            letterSpacing = 0.1.sp,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
        )
        Row {

        }
    }
}

