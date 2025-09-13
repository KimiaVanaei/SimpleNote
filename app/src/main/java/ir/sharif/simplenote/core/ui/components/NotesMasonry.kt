package ir.sharif.simplenote.feature.note.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.core.ui.components.NoteCard
import ir.sharif.simplenote.feature.note.domain.model.Note

@Composable
fun NotesTwoColumnMasonry(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val left  = notes.filterIndexed { i, _ -> i % 2 == 0 }
    val right = notes.filterIndexed { i, _ -> i % 2 == 1 }

    Row(
        modifier = modifier.fillMaxSize().padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(left, key = { it.id }) { n -> NoteCard(n, onClick = { onNoteClick(n) }) }
        }
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(right, key = { it.id }) { n -> NoteCard(n, onClick = { onNoteClick(n) }) }
        }
    }
}
