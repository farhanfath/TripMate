package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.remote.geoapify.model.PlacesResponse
import gli.project.tripmate.domain.model.Place

fun PlacesResponse.toDomain() : List<Place> {
    return this.features.map { feature ->
        val properties = feature.properties

        Place(
            placeId = properties.placeId,
            name = properties.name,
            country = properties.country,
            city = properties.city,
            image = properties.wikiAndMedia?.image ?: "",
            lat = properties.lat,
            lon = properties.lon
        )
    }
}