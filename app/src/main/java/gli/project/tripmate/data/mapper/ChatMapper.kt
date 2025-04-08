package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.remote.gemini.model.GeminiResponse
import gli.project.tripmate.domain.model.chatbot.Candidate
import gli.project.tripmate.domain.model.chatbot.ChatResponse
import gli.project.tripmate.domain.model.chatbot.Content
import gli.project.tripmate.domain.model.chatbot.Part

fun GeminiResponse.toDomain() : ChatResponse {
    return ChatResponse(
        candidates = this.candidates?.map { candidatesItem ->
            Candidate(
                content = Content(
                    parts = candidatesItem.content.parts.map { partsItem ->
                        Part(
                            text = partsItem.text
                        )
                    }
                )
            )
        }
    )
}