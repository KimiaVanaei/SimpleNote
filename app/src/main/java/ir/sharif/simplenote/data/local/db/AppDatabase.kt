package ir.sharif.simplenote.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.sharif.simplenote.data.local.dao.NoteDao
import ir.sharif.simplenote.data.local.entity.NoteEntity


@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}