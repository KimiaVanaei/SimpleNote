package ir.sharif.simplenote.data.remote.dto

data class TokenPair(
    val access: String,
    val refresh: String
)


data class TokenRefreshRequest(val refresh: String)


data class NoteRequestDTO(
    val title: String,
    val description: String
)


data class NoteDTO(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String, // ISO8601
    val updated_at: String, // ISO8601
    val creator_name: String?,
    val creator_username: String?
)


data class PaginatedNoteListDTO(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NoteDTO>
)