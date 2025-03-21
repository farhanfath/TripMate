package gli.project.tripmate.domain.repository

interface ChatRepository {
    suspend fun getChatResponse(message: String): String
}