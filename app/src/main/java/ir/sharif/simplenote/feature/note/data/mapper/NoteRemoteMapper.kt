package ir.sharif.simplenote.feature.note.data.mapper

import ir.sharif.simplenote.feature.note.data.local.NoteEntity
import ir.sharif.simplenote.feature.note.data.remote.NoteDto
import ir.sharif.simplenote.feature.note.domain.model.Note
import ir.sharif.simplenote.core.util.toTimestamp

// Domain -> Remote DTO
fun Note.toDto(): NoteDto {
    return NoteDto(
        id = serverId,
        title = title,
        description = content
    )
}

// Remote DTO -> Domain
fun NoteDto.toDomain(username: String): Note {
    return Note(
        localId = 0, // Gerenated By Room
        serverId = id,
        username = username,
        title = title,
        content = description,
        lastEdited = toTimestamp(updated_at ?: created_at),
        isSynced = true
    )
}

// Remote DTO -> Entity
fun NoteDto.toEntity(username: String): NoteEntity {
    return NoteEntity(
        localId = 0, // autoGenerate in Room
        serverId = id,
        username = username,
        title = title,
        content = description,
        lastEdited = toTimestamp(updated_at ?: created_at),
        isSynced = true
    )
}