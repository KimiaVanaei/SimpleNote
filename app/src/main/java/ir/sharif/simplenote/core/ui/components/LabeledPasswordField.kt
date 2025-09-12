package ir.sharif.simplenote.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.TextStyles

@Composable
fun LabeledPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    imeAction: ImeAction,
    onImeDone: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = TextStyles.textBaseMedium, color = ColorPalette.NeutralBlack)
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyles.textBase,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = onToggleVisibility) {
                    Icon(imageVector = icon, contentDescription = null, tint = ColorPalette.NeutralDarkGrey)
                }
            },
            placeholder = { Text("*********", color = ColorPalette.NeutralBaseGrey) },
            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password, imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = { onImeDone() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ColorPalette.NeutralWhite,
                unfocusedContainerColor = ColorPalette.NeutralWhite,
                disabledContainerColor = ColorPalette.NeutralWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ColorPalette.NeutralBaseGrey, RoundedCornerShape(12.dp))
                .height(54.dp)
        )
    }
}

