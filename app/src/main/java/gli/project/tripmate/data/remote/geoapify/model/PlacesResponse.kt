package gli.project.tripmate.data.remote.geoapify.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PlacesResponse(

    @field:SerializedName("features")
	val features: List<FeaturesItem>,

    @field:SerializedName("type")
	val type: String
) : Parcelable

@Parcelize
data class Historic(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("start_date")
	val startDate: String
) : Parcelable

@Parcelize
data class WikiAndMedia(

	@field:SerializedName("wikipedia")
	val wikipedia: String,

	@field:SerializedName("wikidata")
	val wikidata: String,

	@field:SerializedName("wikimedia_commons")
	val wikimediaCommons: String,

	@field:SerializedName("image")
	val image: String
) : Parcelable

@Parcelize
data class Properties(

    @field:SerializedName("country")
	val country: String,

    @field:SerializedName("name_international")
	val nameInternational: NameInternational,

    @field:SerializedName("wiki_and_media")
	val wikiAndMedia: WikiAndMedia?,

    @field:SerializedName("historic")
	val historic: Historic,

    @field:SerializedName("city")
	val city: String,

    @field:SerializedName("formatted")
	val formatted: String,

    @field:SerializedName("postcode")
	val postcode: String,

    @field:SerializedName("lon")
	val lon: Double,

    @field:SerializedName("country_code")
	val countryCode: String,

    @field:SerializedName("address_line2")
	val addressLine2: String,

    @field:SerializedName("address_line1")
	val addressLine1: String,

    @field:SerializedName("street")
	val street: String,

    @field:SerializedName("datasource")
	val datasource: Datasource,

    @field:SerializedName("district")
	val district: String,

    @field:SerializedName("name")
	val name: String,

    @field:SerializedName("suburb")
	val suburb: String,

    @field:SerializedName("details")
	val details: List<String>,

    @field:SerializedName("state")
	val state: String,

    @field:SerializedName("categories")
	val categories: List<String>,

    @field:SerializedName("village")
	val village: String,

    @field:SerializedName("city_district")
	val cityDistrict: String,

    @field:SerializedName("lat")
	val lat: Double,

    @field:SerializedName("place_id")
	val placeId: String,

    @field:SerializedName("city_block")
	val cityBlock: String,

    @field:SerializedName("description")
	val description: String,

    @field:SerializedName("building")
	val building: Building,

    @field:SerializedName("restrictions")
	val restrictions: Restrictions,

    @field:SerializedName("opening_hours")
	val openingHours: String,

    @field:SerializedName("name_other")
	val nameOther: NameOther
) : Parcelable

@Parcelize
data class NameInternational(

	@field:SerializedName("ja")
	val ja: String,

	@field:SerializedName("en")
	val en: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("cs")
	val cs: String,

	@field:SerializedName("de")
	val de: String,

	@field:SerializedName("vi")
	val vi: String
) : Parcelable

@Parcelize
data class Restrictions(

	@field:SerializedName("access")
	val access: String
) : Parcelable

@Parcelize
data class Raw(

	@field:SerializedName("osm_id")
	val osmId: Long,

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("historic")
	val historic: String,

	@field:SerializedName("name:ja")
	val nameJa: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("name:id")
	val nameId: String,

	@field:SerializedName("name:en")
	val nameEn: String,

	@field:SerializedName("wikipedia")
	val wikipedia: String,

	@field:SerializedName("wikipedia:en")
	val wikipediaEn: String,

	@field:SerializedName("wikidata")
	val wikidata: String,

	@field:SerializedName("wikimedia_commons")
	val wikimediaCommons: String,

	@field:SerializedName("official_name")
	val officialName: String,

	@field:SerializedName("access")
	val access: String,

	@field:SerializedName("alt_name:id")
	val altNameId: String,

	@field:SerializedName("official_name:cs")
	val officialNameCs: String,

	@field:SerializedName("official_name:en")
	val officialNameEn: String,

	@field:SerializedName("tourism")
	val tourism: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("addr:postcode")
	val addrPostcode: Int,

	@field:SerializedName("building")
	val building: String,

	@field:SerializedName("official_name:de")
	val officialNameDe: String,

	@field:SerializedName("name:cs")
	val nameCs: String,

	@field:SerializedName("start_date")
	val startDate: String,

	@field:SerializedName("building:material")
	val buildingMaterial: String,

	@field:SerializedName("name:vi")
	val nameVi: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("name:de")
	val nameDe: String,

	@field:SerializedName("alt_name")
	val altName: String,

	@field:SerializedName("opening_hours")
	val openingHours: String,

	@field:SerializedName("short_name")
	val shortName: String,

	@field:SerializedName("official_name:id")
	val officialNameId: String,

	@field:SerializedName("mapillary")
	val mapillary: Long
) : Parcelable

@Parcelize
data class Geometry(

	@field:SerializedName("coordinates")
	val coordinates: List<Double>,

	@field:SerializedName("type")
	val type: String
) : Parcelable

@Parcelize
data class FeaturesItem(

    @field:SerializedName("geometry")
	val geometry: Geometry,

    @field:SerializedName("type")
	val type: String,

    @field:SerializedName("properties")
	val properties: Properties
) : Parcelable

@Parcelize
data class Building(

	@field:SerializedName("material")
	val material: String,

	@field:SerializedName("start_date")
	val startDate: String
) : Parcelable

@Parcelize
data class Datasource(

    @field:SerializedName("license")
	val license: String,

    @field:SerializedName("attribution")
	val attribution: String,

    @field:SerializedName("raw")
	val raw: Raw,

    @field:SerializedName("sourcename")
	val sourcename: String,

    @field:SerializedName("url")
	val url: String
) : Parcelable

@Parcelize
data class NameOther(

	@field:SerializedName("official_name")
	val officialName: String,

	@field:SerializedName("alt_name")
	val altName: String,

	@field:SerializedName("short_name")
	val shortName: String
) : Parcelable
