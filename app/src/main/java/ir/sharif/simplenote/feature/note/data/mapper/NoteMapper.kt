package ir.sharif.simplenote.feature.note.data.mapper

import ir.sharif.simplenote.feature.note.data.local.NoteEntity
import ir.sharif.simplenote.feature.note.domain.model.Note

// Entity -> Domain
fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        username = username,
        title = title,
        content = content,
        lastEdited = lastEdited,
        isSynced = isSynced
    )
}

fun Note.toEntity(username: String): NoteEntity {
    return NoteEntity(
        id = id,
        username = username,
        title = title,
        content = content,
        lastEdited = lastEdited,
        isSynced = isSynced
    )
}