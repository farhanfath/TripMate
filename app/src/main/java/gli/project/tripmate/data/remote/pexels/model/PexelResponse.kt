package gli.project.tripmate.data.remote.pexels.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PexelResponse(

	@field:SerializedName("next_page")
	val nextPage: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("photos")
	val photos: List<PhotosItem>,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
) : Parcelable

@Parcelize
data class PhotosItem(

	@field:SerializedName("src")
	val src: Src? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("avg_color")
	val avgColor: String? = null,

	@field:SerializedName("alt")
	val alt: String? = null,

	@field:SerializedName("photographer")
	val photographer: String? = null,

	@field:SerializedName("photographer_url")
	val photographerUrl: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("photographer_id")
	val photographerId: Int? = null,

	@field:SerializedName("liked")
	val liked: Boolean? = null,

	@field:SerializedName("height")
	val height: Int? = null
) : Parcelable

@Parcelize
data class Src(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("large")
	val large: String? = null,

	@field:SerializedName("tiny")
	val tiny: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null,

	@field:SerializedName("large2x")
	val large2x: String? = null,

	@field:SerializedName("portrait")
	val portrait: String? = null,

	@field:SerializedName("landscape")
	val landscape: String? = null
) : Parcelable
