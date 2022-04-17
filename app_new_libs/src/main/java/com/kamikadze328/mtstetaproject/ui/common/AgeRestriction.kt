package com.kamikadze328.mtstetaproject.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AgeRestriction(
    modifier: Modifier = Modifier,
    value: String,
    size: Dp = 54.dp,
    fontSize: TextUnit = 17.sp
) {
    Box(
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(50)
            )
            .size(size),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = value,
            fontSize = fontSize
        )
    }
}