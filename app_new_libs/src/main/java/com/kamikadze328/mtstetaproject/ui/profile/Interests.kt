package com.kamikadze328.mtstetaproject.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.presentation.main.CallbackGenreClicked
import com.kamikadze328.mtstetaproject.ui.common.ListGenre

@Composable
fun Interests(
    genres: SnapshotStateList<Genre>,
    genreCallback: CallbackGenreClicked,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Header(textRes = R.string.profile_header_interests)
        ListGenre(
            genres = genres,
            callback = genreCallback,
        )
    }
}