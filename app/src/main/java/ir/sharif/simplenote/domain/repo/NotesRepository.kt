package ir.sharif.simplenote.domain.repo

import ir.sharif.simplenote.domain.model.Note
import kotlinx.coroutines.flow.Flow


interface NotesRepository {
    fun observeNotes(query: String?): Flow<List<Note>>
    suspend fun get(localId: String): Note?
    suspend fun create(title: String, description: String): String // returns localId
    suspend fun update(localId: String, title: String, description: String)
    suspend fun delete(localId: String)
    suspend fun syncOnce(): SyncResult
}


data class SyncResult(
    val pushed: Int,
    val pulled: Int,
    val errors: List<String>
)