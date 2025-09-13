package ir.sharif.simplenote.core.ui.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.MagnifyingGlass
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.XMark
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeHolder: String = "Search...",
    icon: ImageVector = Heroicons.Outline.MagnifyingGlass,
    textFieldIcon: ImageVector? = null,
    onTextFieldIconClick: () -> Unit = {}
) {
    Row(
        modifier.fillMaxWidth().padding(16.dp, 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            modifier = Modifier.size(24.dp),
            onClick = onIconClick,
            enabled = query.isNotEmpty()
        ) {
            Icon(icon, contentDescription = "search")
        }

        Surface(
            modifier = Modifier
                .weight(1f).padding(vertical = 20.dp),
            color = ColorPalette.NeutralLightGrey,
            shape = RoundedCornerShape(8.dp)
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = TextStyles.textSm.copy(color = ColorPalette.NeutralBlack),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = { onIconClick() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = placeHolder,
                                    style = TextStyles.textSm.copy(color = ColorPalette.NeutralBaseGrey),
                                )
                            }
                            innerTextField()
                        }
                        if (query.isNotEmpty() && textFieldIcon != null) {
                            IconButton(
                                modifier = Modifier.size(24.dp),
                                onClick = {
                                    onTextFieldIconClick()
                                    onQueryChange("")
                                }
                            ) { Icon(textFieldIcon, contentDescription = "clear") }
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    val (q, setQ) = remember { mutableStateOf("") }
    SimpleNoteTheme {
        SearchBar(
            query = q,
            onQueryChange = setQ,
            onIconClick = {},
            textFieldIcon = Heroicons.Outline.XMark
        )
    }
}
