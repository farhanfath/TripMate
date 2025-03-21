package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.repository.ChatRepository
import gli.project.tripmate.domain.usecase.ChatUseCase
import javax.inject.Inject

class ChatUseCaseImpl @Inject constructor(
    private val chatRepository: ChatRepository
) : ChatUseCase {
    override suspend fun getChatResponse(message: String): String {
        return chatRepository.getChatResponse(message)
    }
}