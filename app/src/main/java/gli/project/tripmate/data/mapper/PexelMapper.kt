package gli.project.tripmate.data.mapper

import gli.project.tripmate.data.remote.pexels.model.PhotosItem
import gli.project.tripmate.domain.model.PexelImage

//fun PexelResponse.toDomain(): List<PexelImage> {
//    return this.photos.map { photosItem ->
//        val image = photosItem.src
//
//        PexelImage(
//            originalSize = image?.original ?: "",
//            largeSize = image?.large ?: "",
//            mediumSize = image?.medium ?: "",
//            smallSize = image?.small ?: "",
//            tinySize = image?.tiny ?: "",
//            portraitSize = image?.portrait ?: "",
//            landscapeSize = image?.landscape ?: ""
//        )
//    }
//}

fun PhotosItem.toDomain(): PexelImage {
    val image = this.src

    return PexelImage(
        originalSize = image?.original ?: "",
        largeSize = image?.large ?: "",
        mediumSize = image?.medium ?: "",
        smallSize = image?.small ?: "",
        tinySize = image?.tiny ?: "",
        portraitSize = image?.portrait ?: "",
        landscapeSize = image?.landscape ?: ""
    )
}

//fun PexelResponse.toDomain(): PexelImage {
//    val photos = this.photos.firstOrNull() ?: throw IllegalStateException("No photos found in response")
//    val image = photos.src
//
//    return PexelImage(
//        originalSize = image?.original ?: "",
//        largeSize = image?.large ?: "",
//        mediumSize = image?.medium ?: "",
//        smallSize = image?.small ?: "",
//        tinySize = image?.tiny ?: "",
//        portraitSize = image?.portrait ?: "",
//        landscapeSize = image?.landscape ?: ""
//    )
//}