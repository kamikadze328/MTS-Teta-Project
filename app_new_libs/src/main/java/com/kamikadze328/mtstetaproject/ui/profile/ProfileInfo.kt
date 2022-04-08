package com.kamikadze328.mtstetaproject.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.ui.theme.profile_special_background

@Composable
fun ProfileInfo(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = user.email,
                fontSize = 14.sp,
            )
        }
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.profile_img_size))
                .clip(CircleShape)
                .background(profile_special_background),
        ) {
            Image(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.profile_img_size))
                    .height(dimensionResource(R.dimen.profile_img_size))
                    .padding(top = 24.dp, bottom = 20.dp),
                painter = painterResource(R.drawable.ic_profile_img_default),
                contentDescription = stringResource(R.string.profile_img_description)
            )
        }
    }
}