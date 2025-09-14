package ir.sharif.simplenote.feature.note.data.remote

import retrofit2.http.*

data class NoteDto(
    val id: Int? = null,
    val title: String,
    val description: String,            // equivalent to local field "content"
    val created_at: String? = null,
    val updated_at: String? = null,
    val creator_name: String? = null,
    val creator_username: String? = null
)

data class NotesListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NoteDto>
)

interface NoteApi {

    @GET("api/notes/")
    suspend fun getNotes(): NotesListResponse

    @POST("api/notes/")
    suspend fun createNote(@Body note: NoteDto): NoteDto

    @GET("api/notes/{id}/")
    suspend fun getNoteById(@Path("id") id: Int): NoteDto

    @PUT("api/notes/{id}/")
    suspend fun updateNote(
        @Path("id") id: Int,
        @Body note: NoteDto
    ): NoteDto

    @PATCH("api/notes/{id}/")
    suspend fun partialUpdateNote(
        @Path("id") id: Int,
        @Body fields: Map<String, Any>
    ): NoteDto

    @DELETE("api/notes/{id}/")
    suspend fun deleteNote(@Path("id") id: Int)

    @POST("api/notes/bulk")
    suspend fun bulkCreate(@Body notes: List<NoteDto>): List<NoteDto>

    @GET("api/notes/filter")
    suspend fun filterNotes(): NotesListResponse
}
