package ir.sharif.simplenote.feature.note.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import ir.sharif.simplenote.feature.note.data.local.AppDatabase
import ir.sharif.simplenote.feature.note.data.repository.NotesRepositoryImpl
import ir.sharif.simplenote.feature.note.domain.repository.NotesRepository
import ir.sharif.simplenote.feature.note.domain.usecase.AddNoteUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.DeleteNoteUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.GetNoteByIdUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.ObserveNotesUseCase
import ir.sharif.simplenote.feature.note.domain.usecase.UpdateNoteUseCase
import ir.sharif.simplenote.feature.note.presentation.NoteEditorViewModel
import ir.sharif.simplenote.feature.note.presentation.NotesViewModel
import ir.sharif.simplenote.core.util.UserProfileRepoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE notes ADD COLUMN username TEXT NOT NULL DEFAULT ''"
        )
    }
}

object NotesGraph {
    @Volatile private var db: AppDatabase? = null

    private fun db(context: Context): AppDatabase =
        db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "notes.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build().also { db = it }
        }

    fun repo(context: Context): NotesRepository =
        NotesRepositoryImpl(db(context).noteDao())

    // -------- ViewModel factories ----------
    fun notesVmFactory(context: Context) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = repo(context)
            val userProfileRepo = UserProfileRepoProvider.get(context, CoroutineScope(Dispatchers.IO))

            val vm = NotesViewModel(
                userProfileRepo = userProfileRepo,
                observeNotes = ObserveNotesUseCase(repo),
                addNote      = AddNoteUseCase(repo),
                updateNote   = UpdateNoteUseCase(repo),
                deleteNote   = DeleteNoteUseCase(repo),
            )
            @Suppress("UNCHECKED_CAST") return vm as T
        }
    }

    fun editorVmFactory(context: Context, noteId: Int?) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val repo = repo(context)
            val userProfileRepo = UserProfileRepoProvider.get(context, CoroutineScope(Dispatchers.IO))

            val vm = NoteEditorViewModel(
                userProfileRepo = userProfileRepo,
                getNote   = GetNoteByIdUseCase(repo),
                addNote   = AddNoteUseCase(repo),
                updateNote= UpdateNoteUseCase(repo),
                deleteNote= DeleteNoteUseCase(repo),
                initialId = noteId
            )
            @Suppress("UNCHECKED_CAST") return vm as T
        }
    }
}
