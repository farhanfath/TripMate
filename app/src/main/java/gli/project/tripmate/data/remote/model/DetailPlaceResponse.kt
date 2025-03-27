package gli.project.tripmate.data.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailPlaceResponse(

	@field:SerializedName("features")
	val features: List<DetailFeaturesItem>,

	@field:SerializedName("type")
	val type: String
) : Parcelable

@Parcelize
data class Contact(

	@field:SerializedName("phone")
	val phone: String
) : Parcelable

@Parcelize
data class DetailRaw(

	@field:SerializedName("brand:wikidata")
	val brandWikidata: String,

	@field:SerializedName("wheelchair")
	val wheelchair: String,

	@field:SerializedName("payment:notes")
	val paymentNotes: String,

	@field:SerializedName("shop")
	val shop: String,

	@field:SerializedName("payment:credit_cards")
	val paymentCreditCards: String,

	@field:SerializedName("air_conditioning")
	val airConditioning: String,

	@field:SerializedName("building:levels")
	val buildingLevels: Int,

	@field:SerializedName("addr:postcode")
	val addrPostcode: Int,

	@field:SerializedName("building")
	val building: String,

	@field:SerializedName("check_date")
	val checkDate: String,

	@field:SerializedName("second_hand")
	val secondHand: String,

	@field:SerializedName("stroller")
	val stroller: String,

	@field:SerializedName("brand")
	val brand: String,

	@field:SerializedName("organic")
	val organic: String,

	@field:SerializedName("osm_id")
	val osmId: Int,

	@field:SerializedName("website")
	val website: String,

	@field:SerializedName("internet_access")
	val internetAccess: String,

	@field:SerializedName("brand:wikipedia")
	val brandWikipedia: String,

	@field:SerializedName("currency:EUR")
	val currencyEUR: String,

	@field:SerializedName("payment:cash")
	val paymentCash: String,

	@field:SerializedName("payment:contactless")
	val paymentContactless: String,

	@field:SerializedName("payment:visa")
	val paymentVisa: String,

	@field:SerializedName("addr:city")
	val addrCity: String,

	@field:SerializedName("osm_type")
	val osmType: String,

	@field:SerializedName("addr:housenumber")
	val addrHousenumber: Int,

	@field:SerializedName("payment:girocard")
	val paymentGirocard: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("opening_hours")
	val openingHours: String,

	@field:SerializedName("payment:coins")
	val paymentCoins: String,

	@field:SerializedName("addr:street")
	val addrStreet: String
) : Parcelable

@Parcelize
data class DetailDatasource(

	@field:SerializedName("license")
	val license: String,

	@field:SerializedName("attribution")
	val attribution: String,

	@field:SerializedName("raw")
	val raw: DetailRaw,

	@field:SerializedName("sourcename")
	val sourcename: String,

	@field:SerializedName("url")
	val url: String
) : Parcelable

@Parcelize
data class Facilities(

	@field:SerializedName("wheelchair")
	val wheelchair: Boolean,

	@field:SerializedName("internet_access")
	val internetAccess: Boolean,

	@field:SerializedName("air_conditioning")
	val airConditioning: Boolean
) : Parcelable

@Parcelize
data class DetailGeometry(

	@field:SerializedName("coordinates")
	val coordinates: List<List<List<Double>>>,

	@field:SerializedName("type")
	val type: String
) : Parcelable

@Parcelize
data class Commercial(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("organic")
	val organic: Boolean
) : Parcelable

@Parcelize
data class DetailBuilding(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("levels")
	val levels: Int
) : Parcelable

@Parcelize
data class Timezone(

	@field:SerializedName("offset_DST")
	val offsetDST: String,

	@field:SerializedName("offset_DST_seconds")
	val offsetDSTSeconds: Int,

	@field:SerializedName("offset_STD_seconds")
	val offsetSTDSeconds: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("abbreviation_DST")
	val abbreviationDST: String,

	@field:SerializedName("offset_STD")
	val offsetSTD: String,

	@field:SerializedName("abbreviation_STD")
	val abbreviationSTD: String
) : Parcelable

@Parcelize
data class DetailFeaturesItem(

	@field:SerializedName("geometry")
	val geometry: DetailGeometry,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("properties")
	val properties: DetailProperties
) : Parcelable

@Parcelize
data class DetailProperties(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("commercial")
	val commercial: Commercial,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("formatted")
	val formatted: String,

	@field:SerializedName("timezone")
	val timezone: Timezone,

	@field:SerializedName("county")
	val county: String,

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("building")
	val building: DetailBuilding,

	@field:SerializedName("brand_details")
	val brandDetails: BrandDetails,

	@field:SerializedName("address_line2")
	val addressLine2: String,

	@field:SerializedName("address_line1")
	val addressLine1: String,

	@field:SerializedName("street")
	val street: String,

	@field:SerializedName("contact")
	val contact: Contact,

	@field:SerializedName("categories")
	val categories: List<String>,

	@field:SerializedName("state")
	val state: String,

	@field:SerializedName("brand")
	val brand: String,

	@field:SerializedName("lat")
	val lat: Double,

	@field:SerializedName("place_id")
	val placeId: String,

	@field:SerializedName("payment_options")
	val paymentOptions: PaymentOptions,

	@field:SerializedName("website")
	val website: String,

	@field:SerializedName("postcode")
	val postcode: String,

	@field:SerializedName("feature_type")
	val featureType: String,

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("housenumber")
	val housenumber: String,

	@field:SerializedName("datasource")
	val datasource: DetailDatasource,

	@field:SerializedName("opening_hours")
	val openingHours: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("facilities")
	val facilities: Facilities
) : Parcelable

@Parcelize
data class PaymentOptions(

	@field:SerializedName("notes")
	val notes: Boolean,

	@field:SerializedName("contactless")
	val contactless: Boolean,

	@field:SerializedName("coins")
	val coins: Boolean,

	@field:SerializedName("girocard")
	val girocard: Boolean,

	@field:SerializedName("visa")
	val visa: Boolean,

	@field:SerializedName("credit_cards")
	val creditCards: Boolean,

	@field:SerializedName("cash")
	val cash: Boolean
) : Parcelable

@Parcelize
data class BrandDetails(

	@field:SerializedName("wikipedia")
	val wikipedia: String,

	@field:SerializedName("wikidata")
	val wikidata: String
) : Parcelable
