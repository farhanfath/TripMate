package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.remote.model.DetailPlaceResponse
import gli.project.tripmate.domain.model.DetailPlace

fun DetailPlaceResponse.toDomain() : DetailPlace {
    val feature = this.features?.firstOrNull() ?: throw IllegalStateException("No feature found in response")
    val properties = feature.properties

    return DetailPlace(
        placeId = properties?.placeId ?: "",
        name = properties?.name ?: "",
        country = properties?.country ?: "",
        state = properties?.state ?: "",
        city = properties?.city ?: "",
        address = properties?.addressLine2 ?: "",
        openingHours = properties?.openingHours ?: "",
        websiteUrl = properties?.website ?: "",
        imageUrl = properties?.wikiAndMedia?.image ?: "",
        lat = properties?.lat ?: 0.0,
        lon = properties?.lon ?: 0.0
    )
}