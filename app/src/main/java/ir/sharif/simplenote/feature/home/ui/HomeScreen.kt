package ir.sharif.simplenote.feature.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.sharif.simplenote.core.ui.bars.BottomTab
import ir.sharif.simplenote.core.ui.bars.SearchBar
import ir.sharif.simplenote.core.ui.bars.TabBar
import ir.sharif.simplenote.feature.note.di.NotesGraph
import ir.sharif.simplenote.feature.note.presentation.NotesViewModel
import ir.sharif.simplenote.feature.note.ui.NotesTwoColumnMasonry
import ir.sharif.simplenote.core.util.authDataStore

@Composable
fun HomeScreen(
    onOpenSettings: () -> Unit,
    onCreateNote: (Int) -> Unit,
    onOpenNote: (Int) -> Unit,
    onPressHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val vm: NotesViewModel = viewModel(factory = NotesGraph.notesVmFactory(context))
    val ui by vm.ui.collectAsState()

    // Sync
    LaunchedEffect(Unit) {
        vm.sync()
    }

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 110.dp)
        ) {
            if (ui.hasAny) {
                Spacer(Modifier.height(12.dp))

                SearchBar(
                    query = ui.query,
                    onQueryChange = vm::onQueryChange,
                    onIconClick = { vm.onQueryChange(ui.query) },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                if (ui.notes.isNotEmpty()) {
                    NotesTwoColumnMasonry(
                        notes = ui.notes,
                        onNoteClick = { onOpenNote(it.id) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    // No results for current query → show a small hint instead of StartJourney
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            "No notes match your search",
                            // style: use your TextStyles if you want
                        )
                    }
                }
            } else {
                // True empty state (no notes in DB)
                StartJourney(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                )
            }
        }

        TabBar(
            selected = BottomTab.Home,
            onHomeClick = onPressHome,
            onFabClick = { vm.addBlankNote { newId -> onCreateNote(newId) } },
            onSettingsClick = onOpenSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
