package com.kamikadze328.mtstetaproject.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        modifier = modifier.padding(top = 20.dp, bottom = 10.dp),
        text = stringResource(textRes),
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
    )
}

@Composable
fun MainHeader(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
) {
    Header(
        modifier = modifier,
        textRes = textRes,
        fontSize = fontSize,
    )
}