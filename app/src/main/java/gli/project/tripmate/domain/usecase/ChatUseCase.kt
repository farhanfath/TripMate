package gli.project.tripmate.domain.usecase

interface ChatUseCase {
    suspend fun getChatResponse(message: String) : String
}