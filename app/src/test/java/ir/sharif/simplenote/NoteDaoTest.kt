import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ir.sharif.simplenote.data.db.AppDB
import ir.sharif.simplenote.data.db.entity.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Date
import android.content.Context
import ir.sharif.simplenote.data.db.dao.NoteDao
import ir.sharif.simplenote.data.db.entity.User

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NoteDaoTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDB

    @Before
    fun createDb() {
        println("--- Setting up in-memory database ---")
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
        println("--- Database setup complete ---")
    }

    @After
    fun closeDb() {
        println("--- Closing database ---")
        db.close()
        println("--- Database closed ---")
    }

    @Test
    fun insertAndReadNotesWithPagination() = runBlocking {
        println("--- Running pagination test ---")
        val notes = (1..10).map { i ->
            Note(
                title = "Note $i",
                description = "Description $i",
                createdAt = Date(),
                updatedAt = Date(),
                creatorName = "User",
                creatorUsername = "testuser"
            )
        }
        noteDao.insertAll(notes)
        println("Inserted 10 notes.")

        // Get the first 5 notes
        val initialNotes = noteDao.getNotesPaginatedInitial(5).first()
        println("Retrieved ${initialNotes.size} initial notes.")
        assertEquals(5, initialNotes.size)

        val lastNote = initialNotes.last()
        println("Last note in the initial list is Note ${lastNote.id}")

        // Get the next 5 notes using cursor-based pagination
        val nextNotes = noteDao.getNotesPaginated(lastNote.updatedAt, lastNote.id, 5).first()
        println("Retrieved ${nextNotes.size} next notes.")
        assertEquals(5, nextNotes.size)

        println("--- Pagination test passed ---")
    }

    @Test
    fun softDeleteAndRetrieveStaleNotes() = runBlocking {
        println("--- Running soft delete and sync test ---")
        val note1 = Note(title = "Local Note", description = "To be synced", createdAt = Date(), updatedAt = Date(), creatorName = "User", creatorUsername = "testuser", isSynced = false)
        val note2 = Note(title = "Deleted Note", description = "To be soft-deleted", createdAt = Date(), updatedAt = Date(), creatorName = "User", creatorUsername = "testuser", isSynced = false)

        noteDao.insert(note1)
        noteDao.insert(note2)

        // Check that both notes are "stale" (not synced)
        var staleNotes = noteDao.getStaleNotes()
        println("Found ${staleNotes.size} stale notes before soft-delete.")
        assertEquals(2, staleNotes.size)

        val noteToDeleteId = staleNotes.first { it.title == "Deleted Note" }.id

        // Soft delete the second note
        println("Soft-deleting note with ID: $noteToDeleteId")
        noteDao.softDeleteNote(noteToDeleteId)

        // Check sync status again; should still have 2 stale notes
        staleNotes = noteDao.getStaleNotes()
        println("Found ${staleNotes.size} stale notes after soft-delete.")
        assertEquals(2, staleNotes.size) // The deleted note is also stale

        // Verify the note is still in the database but marked as deleted
        val deletedNoteFromDb = noteDao.getNoteById(noteToDeleteId)
        assertNotNull(deletedNoteFromDb)
        println("Verified note is in database and is_deleted is: ${deletedNoteFromDb?.isDeleted}")
        assertEquals(true, deletedNoteFromDb?.isDeleted)

        // Mark the first note as synced
        val noteToSyncId = staleNotes.first { it.title == "Local Note" }.id
        println("Marking note with ID $noteToSyncId as synced.")
        noteDao.markSynced(noteToSyncId)

        // Now only the soft-deleted note should be stale
        staleNotes = noteDao.getStaleNotes()
        println("Found ${staleNotes.size} stale notes after marking one as synced.")
        assertEquals(1, staleNotes.size)

        println("--- Soft delete and sync test passed ---")
    }

    @Test
    fun getNoteByApiId() = runBlocking {
        println("--- Running getNoteByApiId test ---")
        val note = Note(id = 1, title = "API Note", description = "Fetched from API", createdAt = Date(), updatedAt = Date(), apiId = 999, isSynced = true, creatorName = "User", creatorUsername = "testuser")

        println("Inserting note with API ID: ${note.apiId}")
        noteDao.insert(note)

        val retrievedNote = noteDao.getNoteByApiId(999)
        println("Retrieved note by API ID.")
        assertNotNull(retrievedNote)
        assertEquals("API Note", retrievedNote?.title)

        println("--- getNoteByApiId test passed ---")
    }
}