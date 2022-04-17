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
import com.kamikadze328.mtstetaproject.ui.common.AgeRestriction
import com.kamikadze328.mtstetaproject.ui.common.Rating

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
        }
    }
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
    val url = Webservice.BASE_PATH_IMAGE_SMALL_URL + movie.poster_path

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { callback.onMovieClicked(movie.movieId) }
            )
            .padding(horizontal = 5.dp, vertical = 12.dp)
            .padding(3.dp),
    ) {
        MovieImage(
            url = url,
            description = movie.title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Title(value = movie.title)
        Description(value = movie.overview)
        RatingAndAgeRestriction(
            rating = movie.vote_average,
            ageRestriction = movie.age_restriction)
    }
}

@Composable
private fun RatingAndAgeRestriction(
    rating: Double,
    ageRestriction: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(3.dp)
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Rating(value = rating)
        AgeRestriction(
            value = ageRestriction,
            size = 21.dp,
            fontSize = 7.sp,
        )
    }
}

@Composable
private fun MovieImage(
    url: String,
    description: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .placeholder(R.drawable.ic_baseline_image_not_supported_24)
            .transformations(RoundedCornersTransformation(10f))
            .build(),
        modifier = modifier
            .width(dimensionResource(R.dimen.main_poster_width))
            .height(dimensionResource(R.dimen.main_poster_height)),
        contentDescription = description,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun Title(value: String) {
    Text(
        value,
        modifier = Modifier.padding(top = 8.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Description(value: String?) {
    Text(
        value ?: "",
        modifier = Modifier.padding(top = 10.dp),
        fontSize = 12.sp,
        letterSpacing = 0.1.sp,
        maxLines = 6,
        overflow = TextOverflow.Ellipsis,
    )
}

