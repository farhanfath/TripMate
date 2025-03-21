package gli.project.tripmate.presentation.ui.screen.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.ui.component.CustomImageLoader
import gli.project.tripmate.presentation.util.extensions.HandlerResponseCompose

@Composable
fun BoxScope.BackDropImage(
    currentImageHeight: Dp,
    imagePexelState: ResultResponse<PexelImage>
) {
//    if (imageUrl == null) {
//
//    } else {
//        CustomImageLoader(
//            url = imageUrl,
//            scale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(currentImageHeight)
//                .align(Alignment.TopCenter)
//        )
//    }
    HandlerResponseCompose(
        response = imagePexelState,
        onLoading = {

        },
        onSuccess = { image ->
            CustomImageLoader(
                url = image.originalSize,
                scale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentImageHeight)
                    .align(Alignment.TopCenter)
            )
        },
        onError = {

        }
    )
    // gradient color
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(currentImageHeight)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 0f,
                    endY = 1200f
                )
            )
            .align(Alignment.TopCenter)
    )
}