package com.kamikadze328.mtstetaproject.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.presentation.main.CallbackGenreClicked
import com.kamikadze328.mtstetaproject.ui.theme.purple_700

@Composable
fun ListGenre(
    genres: List<Genre>,
    callback: CallbackGenreClicked,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = genres,
        ) { genre ->
            Genre(genre = genre, callback = callback)
        }
    }
}
@Composable
fun Genre(
    genre: Genre,
    callback: CallbackGenreClicked
) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    callback.onGenreClicked(genre.genreId)
                }
            )
            .border(
                width = if (genre.isSelected) 2.dp else 1.dp,
                color = if (genre.isSelected) purple_700 else Color.Black,
                shape = RoundedCornerShape(10.dp)
            ),

        ) {
        Text(
            text = genre.name,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = if (genre.isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }

}