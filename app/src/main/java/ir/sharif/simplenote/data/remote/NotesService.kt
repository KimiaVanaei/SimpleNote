package ir.sharif.simplenote.data.remote

import ir.sharif.simplenote.data.remote.dto.*
import retrofit2.http.*


interface NotesService {
    @GET("/api/notes/")
    suspend fun listNotes(@Query("page") page: Int? = null): PaginatedNoteListDTO


    @GET("/api/notes/filter")
    suspend fun filterNotes(
        @Query("title") title: String? = null,
        @Query("description") description: String? = null,
        @Query("updated__gte") updatedGte: String? = null,
        @Query("updated__lte") updatedLte: String? = null,
        @Query("page") page: Int? = null,
    ): PaginatedNoteListDTO


    @POST("/api/notes/")
    suspend fun create(@Body body: NoteRequestDTO): NoteDTO


    @PATCH("/api/notes/{id}/")
    suspend fun patch(@Path("id") id: Int, @Body body: NoteRequestDTO): NoteDTO


    @DELETE("/api/notes/{id}/")
    suspend fun delete(@Path("id") id: Int)
}


interface AuthService {
    @POST("/api/auth/token/")
    suspend fun obtain(@Body body: Map<String, String>): TokenPair


    @POST("/api/auth/token/refresh/")
    suspend fun refresh(@Body body: TokenRefreshRequest): Map<String, String> // { access }
}