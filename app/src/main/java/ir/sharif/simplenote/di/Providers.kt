package ir.sharif.simplenote.di

import android.content.Context
import androidx.room.Room
import ir.sharif.simplenote.data.local.db.AppDatabase
import ir.sharif.simplenote.data.remote.NetworkModuleFactory
import ir.sharif.simplenote.data.remote.TokenStore
import ir.sharif.simplenote.data.repo.LastSyncStore
import ir.sharif.simplenote.data.repo.NotesRepositoryImpl
import ir.sharif.simplenote.domain.repo.NotesRepository


object Providers {
    fun provideRepository(context: Context, baseUrl: String): NotesRepository {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "simplenote.db").build()
        val tokenStore = TokenStore(context)
        val (_, notesService, _) = NetworkModuleFactory.create(baseUrl, context, tokenStore)
        val lastSync = LastSyncStore(context)
        return NotesRepositoryImpl(db.noteDao(), notesService, lastSync)
    }
}