package ir.sharif.simplenote.feature.note.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles
import ir.sharif.simplenote.core.ui.dialogs.DeleteNoteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    title: String,
    body: String,
    lastEditedMillis: Long,
    onNavigateBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onConfirmDelete: () -> Unit,
) {
    var showDelete by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onNavigateBack)
                    ) {
                        Icon(
                            imageVector = Heroicons.Outline.ChevronLeft,
                            contentDescription = "Back",
                            tint = ColorPalette.PrimaryBase,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = TextStyles.textBaseMedium,
                            color = ColorPalette.PrimaryBase
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPalette.NeutralWhite,
                    titleContentColor = ColorPalette.NeutralBlack
                ),
                modifier = Modifier
                    .height(60.dp)
                    .background(ColorPalette.NeutralWhite)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorPalette.NeutralWhite)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Column(Modifier.fillMaxSize()) {

                // Thin divider under the top bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorPalette.NeutralLightGrey)
                )

                // Scrollable editor area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    // Title
                    TextField(
                        value = title,
                        onValueChange = onTitleChange,
                        placeholder = {
                            Text(
                                "Title",
                                style = TextStyles.textXlBold,
                                color = ColorPalette.NeutralBlack
                            )
                        },
                        textStyle = TextStyles.textXlBold,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = ColorPalette.PrimaryBase
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    // Body
                    TextField(
                        value = body,
                        onValueChange = onBodyChange,
                        placeholder = {
                            Text(
                                "Feel Free to Write Here…",
                                style = TextStyles.textSm,
                                color = ColorPalette.NeutralDarkGrey
                            )
                        },
                        textStyle = TextStyles.textSm,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = ColorPalette.PrimaryBase
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 200.dp) // airy feel
                    )
                }

                // Bottom task bar
                Column {
                    // Thin divider line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ColorPalette.NeutralLightGrey)
                    )

                    TaskBar(
                        lastEditedMillis = lastEditedMillis,
                        onDeleteClick = { showDelete = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (showDelete) {
                DeleteNoteDialog(
                    onDismiss = { showDelete = false },
                    onConfirmDelete = {
                        showDelete = false
                        onConfirmDelete()
                    }
                )
            }
        }
    }
}

/* -------------------- Preview -------------------- */

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun EditNoteScreenPreview() {
    SimpleNoteTheme {
        var title by remember { mutableStateOf("") }
        var body by remember { mutableStateOf("") }

        EditNoteScreen(
            title = title,
            body = body,
            lastEditedMillis = System.currentTimeMillis(),
            onNavigateBack = {},
            onTitleChange = { title = it },
            onBodyChange = { body = it },
            onConfirmDelete = {}
        )
    }
}
