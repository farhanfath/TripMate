package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.local.model.FavoriteEntity
import gli.project.tripmate.domain.model.DetailPlace
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

fun DetailPlace.toFavoriteEntity() : FavoriteEntity {
    return FavoriteEntity(
        id = placeId,
        placeId = placeId,
        placeName = name,
        placeImage = imageUrl?: "",
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