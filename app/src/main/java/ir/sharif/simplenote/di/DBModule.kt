package ir.sharif.simplenote.di

import android.content.Context
import androidx.room.Room
import ir.sharif.simplenote.data.db.AppDB

object DBModule {
    @Volatile
    private var INSTANCE: AppDB? = null

    fun getDatabase(context: Context): AppDB {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDB::class.java,
                "simple_note_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}