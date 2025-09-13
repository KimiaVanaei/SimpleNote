package ir.sharif.simplenote.feature.note.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronLeft
import ir.sharif.simplenote.core.designsystem.ColorPalette
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.core.designsystem.TextStyles
import ir.sharif.simplenote.core.ui.bars.TaskBar
import ir.sharif.simplenote.core.ui.dialogs.DeleteNoteDialog
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

private val AppBarHeight = 54.dp     // taller bar → divider sits lower
private val ExtraTopGap = 4.dp       // extra distance from status bar to content

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
    onAutoSave: () -> Unit,
) {
    var showDelete by rememberSaveable { mutableStateOf(false) }

    // System back → delegate to VM (which will save-or-delete)
    BackHandler { onNavigateBack() }

    // 🚀 Auto-save while typing (debounced)
    LaunchedEffect(title, body) {
        snapshotFlow { title to body }
            .debounce(600)                    // wait for typing to pause
            .distinctUntilChanged()           // only when text actually changed
            .map { (t, b) -> t.isNotBlank() || b.isNotBlank() }
            .filter { it }                    // avoid saving fully empty note repeatedly
            .collect { onAutoSave() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 12.dp)
                            .clickable(onClick = onNavigateBack),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Heroicons.Outline.ChevronLeft,
                                contentDescription = "Back",
                                tint = ColorPalette.PrimaryBase,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Back",
                                style = TextStyles.textBaseMedium,
                                color = ColorPalette.PrimaryBase
                            )
                        }
                    }
                },
                // no actions → no explicit Save button (auto-save enabled)
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPalette.NeutralWhite,
                    titleContentColor = ColorPalette.NeutralBlack
                ),
                // Control insets + height + top gap to move content away from status bar
                windowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier
                    .statusBarsPadding()
                    .height(AppBarHeight)
                    .padding(top = ExtraTopGap)
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

                // Divider under top bar
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
                        onValueChange = onTitleChange,    // triggers auto-save pipeline
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
                        onValueChange = onBodyChange,     // triggers auto-save pipeline
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
                            .defaultMinSize(minHeight = 200.dp)
                    )
                }

                // Bottom task bar (trash opens dialog)
                Column {
                    // Divider
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

            // Confirm delete dialog
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
            onConfirmDelete = {},
            onAutoSave = {}
        )
    }
}
