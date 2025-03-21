package gli.project.tripmate.data.remote.gemini.model

data class ChatRequest(
    val contents: List<Map<String, String>>
)

data class ChatResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

data class Content(
    val parts: List<Part>?
)

data class Part(
    val text: String?
)