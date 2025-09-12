package ir.sharif.simplenote.feature.note.data.mapper

import ir.sharif.simplenote.feature.note.data.local.NoteEntity
import ir.sharif.simplenote.feature.note.domain.model.Note

// Entity -> Domain
fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        color = color,
        lastEdited = lastEdited,
        isSynced = isSynced
    )
}

// Domain -> Entity
fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        color = color,
        lastEdited = lastEdited,
        isSynced = isSynced
    )
}