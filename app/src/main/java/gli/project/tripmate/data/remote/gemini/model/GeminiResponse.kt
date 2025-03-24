package gli.project.tripmate.data.remote.gemini.model

import com.google.gson.annotations.SerializedName

data class GeminiResponse(

	@field:SerializedName("candidates")
	val candidates: List<CandidatesItem>?,

	@field:SerializedName("modelVersion")
	val modelVersion: String,

	@field:SerializedName("usageMetadata")
	val usageMetadata: UsageMetadata
)

data class UsageMetadata(

	@field:SerializedName("candidatesTokenCount")
	val candidatesTokenCount: Int,

	@field:SerializedName("promptTokensDetails")
	val promptTokensDetails: List<PromptTokensDetailsItem>,

	@field:SerializedName("totalTokenCount")
	val totalTokenCount: Int,

	@field:SerializedName("promptTokenCount")
	val promptTokenCount: Int,

	@field:SerializedName("candidatesTokensDetails")
	val candidatesTokensDetails: List<CandidatesTokensDetailsItem>
)

data class CandidatesTokensDetailsItem(

	@field:SerializedName("modality")
	val modality: String,

	@field:SerializedName("tokenCount")
	val tokenCount: Int
)

data class PartsItem(

	@field:SerializedName("text")
	val text: String?
)

data class CandidatesItem(

	@field:SerializedName("avgLogprobs")
	val avgLogprobs: Any,

	@field:SerializedName("finishReason")
	val finishReason: String,

	@field:SerializedName("content")
	val content: Content
)

data class PromptTokensDetailsItem(

	@field:SerializedName("modality")
	val modality: String,

	@field:SerializedName("tokenCount")
	val tokenCount: Int
)

data class Content(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("parts")
	val parts: List<PartsItem>
)
