package com.kamikadze328.mtstetaproject.ui.movie.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Actor
import com.kamikadze328.mtstetaproject.data.remote.Webservice
import com.kamikadze328.mtstetaproject.presentation.main.CallbackActorClicked

@Composable
fun ListActors(
    actors: List<Actor>,
    callback: CallbackActorClicked,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = actors,
        ) { actor ->
            Actor(actor = actor, callback = callback)
        }
    }
}

@Composable
fun Actor(
    actor: Actor,
    callback: CallbackActorClicked
) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    callback.onActorClicked(actor.id)
                }
            ),

        ) {
        ActorImage(
            url = Webservice.BASE_PATH_IMAGE_SMALL_URL + actor.profile_path,
            description = actor.name
        )
        Text(
            text = actor.name,
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun ActorImage(
    modifier: Modifier = Modifier,
    url: String,
    description: String
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .transformations(RoundedCornersTransformation(10f))
            .build(),
        contentDescription = description,
        modifier = modifier
            .width(dimensionResource(R.dimen.actor_container_width))
            .height(dimensionResource(R.dimen.actor_container_height)),
        contentScale = ContentScale.Fit,
    )
}


