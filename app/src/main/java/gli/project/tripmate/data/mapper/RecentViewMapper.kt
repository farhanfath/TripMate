package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.local.model.RecentViewEntity
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.domain.model.local.RecentView

fun RecentViewEntity.toDomain() : RecentView {
    return RecentView(
        recentViewId = id,
        placeId = placeId,
        placeName = placeName,
        placeImage = placeImage,
        location = location,
        timeStamp = timeStamp
    )
}

fun Place.toEntity() : RecentViewEntity {
    return RecentViewEntity(
        placeId = placeId,
        placeName = name,
        placeImage = image,
        location = "$city + $country"
    )
}

fun RecentView.toEntity() : RecentViewEntity {
    return RecentViewEntity(
        id = recentViewId,
        placeId = placeId,
        placeName = placeName,
        placeImage = placeImage,
        location = location,
        timeStamp = timeStamp
    )
}