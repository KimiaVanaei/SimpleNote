package ir.sharif.simplenote.feature.note.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY lastEdited DESC")
    fun observeAll(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes ORDER BY lastEdited DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("""
        SELECT * FROM notes
        WHERE title   LIKE '%' || :query || '%'
           OR content LIKE '%' || :query || '%'
        ORDER BY lastEdited DESC
    """)
    suspend fun searchNotes(query: String): List<NoteEntity>
}
