package com.kamikadze328.mtstetaproject.ui.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.Genre
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.presentation.profile.ProfileViewModel
import com.kamikadze328.mtstetaproject.ui.common.Header
import com.kamikadze328.mtstetaproject.ui.theme.profile_special_background

@Composable
fun Profile(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user = when (val userState = viewModel.userState.observeAsState().value) {
        is UIState.DataState -> userState.data
        UIState.LoadingState -> viewModel.loadUserLoading()
        is UIState.ErrorState -> viewModel.loadUserError()
        null -> viewModel.loadUserError()
    }
    val genres: SnapshotStateList<Genre> =
        when (val genreState = viewModel.favouriteGenresState.observeAsState().value) {
            is UIState.DataState -> genreState.data
            UIState.LoadingState -> viewModel.loadGenreLoading()
            is UIState.ErrorState -> viewModel.loadGenreError()
            null -> viewModel.loadGenreError()
        }
    val settings = stringArrayResource(R.array.settings_headers)
    val appSettings = stringArrayResource(R.array.app_info_headers)

    LazyColumn(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        item {
            ProfileInfo(
                modifier = Modifier.padding(top = 48.dp),
                user = user,
            )
            Interests(
                genres = genres,
                genreCallback = viewModel
            )
            ProfileEditableInfo(
                user = user,
                onNameChange = viewModel::updateUserName,
                onPasswordChange = viewModel::updateUserPassword,
                onEmailChange = viewModel::updateUserEmail,
                onPhoneChange = viewModel::updateUserPhone,
            )
            Header(textRes = R.string.profile_header_settings)
        }
        items(settings) { settingsItem ->
            SettingsItem(text = settingsItem)
        }
        item {
            Header(textRes = R.string.profile_header_app_info)
        }
        items(appSettings) { settingsItem ->
            SettingsItem(text = settingsItem)
        }
        item {
            LogoutButton(
                modifier = Modifier.padding(bottom = 48.dp, top = 20.dp),
                onClick = { viewModel.logout() },
            )
        }
    }
}

@Composable
private fun LogoutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = profile_special_background
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            modifier = Modifier.padding(15.dp),
            text = stringResource(R.string.profile_logout_button),
            fontSize = 16.sp,
            color = Color.Black,
        )
    }
}
