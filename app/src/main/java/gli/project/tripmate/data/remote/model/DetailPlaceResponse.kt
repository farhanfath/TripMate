package gli.project.tripmate.data.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailPlaceResponse(

	@field:SerializedName("features")
	val features: List<DetailFeatureItem?>? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable

@Parcelize
data class Timezone(

	@field:SerializedName("offset_DST")
	val offsetDST: String? = null,

	@field:SerializedName("offset_DST_seconds")
	val offsetDSTSeconds: Int? = null,

	@field:SerializedName("offset_STD_seconds")
	val offsetSTDSeconds: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("abbreviation_DST")
	val abbreviationDST: String? = null,

	@field:SerializedName("offset_STD")
	val offsetSTD: String? = null,

	@field:SerializedName("abbreviation_STD")
	val abbreviationSTD: String? = null
) : Parcelable

@Parcelize
data class DetailDataSource(

	@field:SerializedName("license")
	val license: String? = null,

	@field:SerializedName("attribution")
	val attribution: String? = null,

	@field:SerializedName("raw")
	val raw: DetailRaw? = null,

	@field:SerializedName("sourcename")
	val sourcename: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class DetailProperties(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("name_international")
	val nameInternational: DetailNameInternational? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("formatted")
	val formatted: String? = null,

	@field:SerializedName("timezone")
	val timezone: Timezone? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("building")
	val building: DetailBuilding? = null,

	@field:SerializedName("address_line2")
	val addressLine2: String? = null,

	@field:SerializedName("address_line1")
	val addressLine1: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("categories")
	val categories: List<String?>? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("website")
	val website: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null,

	@field:SerializedName("wiki_and_media")
	val wikiAndMedia: DetailWikiAndMedia? = null,

	@field:SerializedName("historic")
	val historic: DetailHistoric? = null,

	@field:SerializedName("postcode")
	val postcode: String? = null,

	@field:SerializedName("restrictions")
	val restrictions: DetailRestrictions? = null,

	@field:SerializedName("feature_type")
	val featureType: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("datasource")
	val datasource: DetailDataSource? = null,

	@field:SerializedName("opening_hours")
	val openingHours: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("name_other")
	val nameOther: DetailNameOther? = null
) : Parcelable

@Parcelize
data class DetailWikiAndMedia(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("wikipedia")
	val wikipedia: String? = null,

	@field:SerializedName("wikidata")
	val wikidata: String? = null,

	@field:SerializedName("wikimedia_commons")
	val wikimediaCommons: String? = null
) : Parcelable

@Parcelize
data class DetailRestrictions(

	@field:SerializedName("access")
	val access: String? = null
) : Parcelable

@Parcelize
data class DetailNameInternational(

	@field:SerializedName("cs")
	val cs: String? = null,

	@field:SerializedName("de")
	val de: String? = null,

	@field:SerializedName("vi")
	val vi: String? = null,

	@field:SerializedName("ja")
	val ja: String? = null,

	@field:SerializedName("en")
	val en: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable

@Parcelize
data class DetailHistoric(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
) : Parcelable

@Parcelize
data class DetailNameOther(

	@field:SerializedName("official_name")
	val officialName: String? = null,

	@field:SerializedName("alt_name")
	val altName: String? = null,

	@field:SerializedName("short_name")
	val shortName: String? = null
) : Parcelable

@Parcelize
data class DetailGeometry(

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable

@Parcelize
data class DetailFeatureItem(

	@field:SerializedName("geometry")
	val geometry: DetailGeometry? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("properties")
	val properties: DetailProperties? = null
) : Parcelable

@Parcelize
data class DetailRaw(

	@field:SerializedName("official_name")
	val officialName: String? = null,

	@field:SerializedName("access")
	val access: String? = null,

	@field:SerializedName("alt_name:id")
	val altNameId: String? = null,

	@field:SerializedName("official_name:cs")
	val officialNameCs: String? = null,

	@field:SerializedName("name:id")
	val nameId: String? = null,

	@field:SerializedName("official_name:en")
	val officialNameEn: String? = null,

	@field:SerializedName("tourism")
	val tourism: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("addr:postcode")
	val addrPostcode: Int? = null,

	@field:SerializedName("building")
	val building: String? = null,

	@field:SerializedName("official_name:de")
	val officialNameDe: String? = null,

	@field:SerializedName("name:en")
	val nameEn: String? = null,

	@field:SerializedName("wikipedia")
	val wikipedia: String? = null,

	@field:SerializedName("name:cs")
	val nameCs: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null,

	@field:SerializedName("building:material")
	val buildingMaterial: String? = null,

	@field:SerializedName("osm_id")
	val osmId: Int? = null,

	@field:SerializedName("name:vi")
	val nameVi: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("historic")
	val historic: String? = null,

	@field:SerializedName("name:ja")
	val nameJa: String? = null,

	@field:SerializedName("wikimedia_commons")
	val wikimediaCommons: String? = null,

	@field:SerializedName("osm_type")
	val osmType: String? = null,

	@field:SerializedName("name:de")
	val nameDe: String? = null,

	@field:SerializedName("alt_name")
	val altName: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("opening_hours")
	val openingHours: String? = null,

	@field:SerializedName("short_name")
	val shortName: String? = null,

	@field:SerializedName("official_name:id")
	val officialNameId: String? = null,

	@field:SerializedName("mapillary")
	val mapillary: Long? = null,

	@field:SerializedName("wikidata")
	val wikidata: String? = null
) : Parcelable

@Parcelize
data class DetailBuilding(

	@field:SerializedName("material")
	val material: String? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
) : Parcelable
