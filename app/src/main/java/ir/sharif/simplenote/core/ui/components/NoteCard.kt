package ir.sharif.simplenote.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.sharif.simplenote.core.designsystem.SimpleNoteTheme
import ir.sharif.simplenote.feature.note.domain.model.Note
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val bg = remember(note.id, note.title, isDark) {
        generatedNoteColor(id = note.id, title = note.title, isDark = isDark)
    }
    val on = if (bg.luminance() > 0.5f) Color.Black else Color.White

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = bg,
            contentColor = on
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title.ifBlank { "Untitled" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = note.content,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.82f)
            )
        }
    }
}

/**
 * Deterministically generates a distinct color per note.
 * - Uses a 64-bit seed to avoid Int overflow.
 * - Distributes hues via the golden-ratio conjugate for nice spacing.
 * - Normalizes hue into 0..360 and clamps S/L into 0..1.
 * - Converts HSL -> Color with our own function (no Color.hsl dependency).
 */
private fun generatedNoteColor(id: Int, title: String, isDark: Boolean): Color {
    val seed64 = (31L * id + title.hashCode().toLong()).absoluteValue
    val frac = ((seed64 * 0.618033988749894) % 1.0).let { if (it < 0) it + 1.0 else it } // 0..1
    val hue = (frac * 360.0).toFloat()                          // 0..360
    val saturation = (if (isDark) 0.58f else 0.38f).coerceIn(0f, 1f)
    val lightness  = (if (isDark) 0.28f else 0.92f).coerceIn(0f, 1f)
    return hslToColor(hue, saturation, lightness)
}

/** Local HSL→Color (safe on all Compose versions) */
private fun hslToColor(h: Float, s: Float, l: Float, a: Float = 1f): Color {
    val hue = ((h % 360f) + 360f) % 360f          // normalize into 0..360
    val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
    val hp = (hue / 60f) % 6f
    val x = c * (1f - kotlin.math.abs(hp % 2f - 1f))
    val (r1, g1, b1) = when {
        hp < 1f -> Triple(c, x, 0f)
        hp < 2f -> Triple(x, c, 0f)
        hp < 3f -> Triple(0f, c, x)
        hp < 4f -> Triple(0f, x, c)
        hp < 5f -> Triple(x, 0f, c)
        else    -> Triple(c, 0f, x)
    }
    val m = l - c / 2f
    val r = clamp01(r1 + m)
    val g = clamp01(g1 + m)
    val b = clamp01(b1 + m)
    return Color(r, g, b, a)
}

private fun clamp01(v: Float) = max(0f, min(1f, v))

private fun demoNotes() = listOf(
    Note(id = 1, username = "demoUser", title = "Groceries", content = "Eggs, milk, coffee, spinach, olive oil, apples", lastEdited = 0L, isSynced = true),
    Note(id = 2, username = "demoUser", title = "Meeting notes", content = "Discuss offline-first sync & Room schema changes.", lastEdited = 0L, isSynced = false),
    Note(id = 3, username = "demoUser", title = "Ideas", content = "Animated checklist, swipe to archive, quick capture tile.", lastEdited = 0L, isSynced = true),
    Note(id = 4, username = "demoUser", title = "Travel", content = "Paris itinerary: Musée d'Orsay, Canal Saint-Martin, picnic.", lastEdited = 0L, isSynced = false),
    Note(id = 5, username = "demoUser", title = "Reading list", content = "Clean Architecture, Kotlin Coroutines, Jetpack Compose.", lastEdited = 0L, isSynced = true),
)

@Preview(name = "NoteCard • Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "NoteCard • Dark",  uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun NoteCardPreview() {
    SimpleNoteTheme {
        NoteCard(note = demoNotes().first(), onClick = {})
    }
}

@Preview(name = "Note list • Light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Note list • Dark",  uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun NoteListPreview() {
    SimpleNoteTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(demoNotes()) { n -> NoteCard(note = n, onClick = {}) }
        }
    }
}
