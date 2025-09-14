package ir.sharif.simplenote.feature.note.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE username = :username ORDER BY lastEdited DESC")
    fun observeAll(username: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE username = :username ORDER BY lastEdited DESC")
    suspend fun getAllNotes(username: String): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE localId = :id AND username = :username LIMIT 1")
    suspend fun getById(id: Int, username: String): NoteEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)

    @Query("""
        SELECT * FROM notes
        WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') AND username = :username ORDER BY lastEdited DESC""")
    suspend fun searchNotes(query: String, username: String): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>)

}