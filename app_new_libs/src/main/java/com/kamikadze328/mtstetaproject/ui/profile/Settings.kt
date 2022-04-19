package com.kamikadze328.mtstetaproject.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamikadze328.mtstetaproject.R

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
        Image(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(R.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = stringResource(R.string.settings_arrow_description)
        )
    }
}