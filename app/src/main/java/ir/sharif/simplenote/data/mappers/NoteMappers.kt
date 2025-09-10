package ir.sharif.simplenote.data.mappers

import ir.sharif.simplenote.data.local.entity.NoteEntity
import ir.sharif.simplenote.data.remote.dto.NoteDTO
import ir.sharif.simplenote.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*


// We use SimpleDateFormat to support minSdk < 26 (no java.time)
private val sdf by lazy {
// Supports both Zulu (UTC) and offset like +01:00
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = true
    }
}


fun NoteEntity.toDomain() = Note(
    localId = localId,
    serverId = serverId,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    creatorName = creatorName,
    creatorUsername = creatorUsername,
    isDirty = isDirty,
    isDeleted = isDeleted
)


fun NoteDTO.toEntity(existingLocalId: String? = null): NoteEntity = NoteEntity(
    localId = existingLocalId ?: UUID.randomUUID().toString(),
    serverId = id,
    title = title,
    description = description,
    createdAt = parseIso(created_at),
    updatedAt = parseIso(updated_at),
    creatorName = creator_name,
    creatorUsername = creator_username,
    isDirty = false,
    isDeleted = false,
    lastModifiedLocal = System.currentTimeMillis(),
    syncError = null
)


fun parseIso(s: String?): Long? = try {
    if (s == null) null else sdf.parse(s)?.time
} catch (_: Exception) { null }


fun toIso(millis: Long?): String? = millis?.let { sdf.format(Date(it)) }