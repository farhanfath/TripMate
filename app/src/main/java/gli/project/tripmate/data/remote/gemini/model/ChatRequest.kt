package gli.project.tripmate.data.remote.gemini.model

data class ChatRequest(
    val contents: List<ChatContent>
)

data class ChatContent(
    val role: String,  // "user" atau "model"
    val parts: List<ChatPart>
)

data class ChatPart(
    val text: String
)