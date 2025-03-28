package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.local.model.FavoriteEntity
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.Favorite

fun FavoriteEntity.toDomain() : Favorite {
    return Favorite(
        favoriteId = id,
        placeId = placeId,
        placeName = placeName,
        placeImage = placeImage,
        location = location,
        timeStamp = timeStamp
    )
}

fun Place.toFavoriteEntity() : FavoriteEntity {
    return FavoriteEntity(
        placeId = placeId,
        placeName = name,
        placeImage = image,
        location = "$city, $country"
    )
}

fun Favorite.toEntity() : FavoriteEntity {
    return FavoriteEntity(
        id = favoriteId,
        placeId = placeId,
        placeName = placeName,
        placeImage = placeImage,
        location = location,
        timeStamp = timeStamp
    )
}