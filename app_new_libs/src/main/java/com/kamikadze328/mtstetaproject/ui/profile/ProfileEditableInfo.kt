package com.kamikadze328.mtstetaproject.ui.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.dto.User

@Composable
fun ProfileEditableInfo(
    user: User,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Header(R.string.profile_header_personal_info)
        EditableTextField(
            value = user.name,
            onValueChange = onNameChange,
            placeholder = R.string.profile_personal_info_name
        )
        EditableTextField(
            value = user.password,
            onValueChange = onPasswordChange,
            placeholder = R.string.password_hint,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
        )
        EditableTextField(
            value = user.email,
            onValueChange = onEmailChange,
            placeholder = R.string.profile_personal_info_email,
            keyboardType = KeyboardType.Email,
        )
        EditableTextField(
            value = user.phone,
            onValueChange = onPhoneChange,
            placeholder = R.string.phone_hint,
            keyboardType = KeyboardType.Phone,
        )
    }
}

@Composable
private fun EditableTextField(
    modifier: Modifier = Modifier,
    value: String,
    @StringRes placeholder: Int,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(placeholder)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(
            fontSize = 14.sp,
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        visualTransformation = visualTransformation,
    )
}