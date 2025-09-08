package ir.sharif.simplenote.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.sharif.simplenote.data.db.converter.Converter
import ir.sharif.simplenote.data.db.dao.NoteDao
import ir.sharif.simplenote.data.db.dao.UserDao
import ir.sharif.simplenote.data.db.entity.Note
import ir.sharif.simplenote.data.db.entity.User

@Database(entities = [Note::class, User::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
}