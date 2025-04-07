package gli.project.tripmate.presentation.ui.component.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import gli.project.tripmate.R

@Composable
fun CustomImageLoader(
    modifier: Modifier = Modifier,
    url: String,
    desc: String? = null,
    scale: ContentScale = ContentScale.Fit
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // List drawable resources untuk placeholder
    val placeholderDrawables = listOf(
        R.drawable.placeholder_hotel,
        R.drawable.placeholder_restaurant,
        R.drawable.placeholder_mall,
        R.drawable.placeholder_cafe
    )

    // Pilih random drawable untuk placeholder error
    val randomPlaceholderRes = remember { placeholderDrawables.random() }

    // Shimmer animation
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )

    // Brush for shimmer effect
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    // Image painter
    val painter = rememberAsyncImagePainter(
        model = url,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        }
    )

    Box(
        modifier = modifier.clip(shape = RectangleShape)
    ) {
        if (!isError) {
            Image(
                painter = painter,
                contentDescription = desc,
                contentScale = scale,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Show shimmer if loading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush)
            )
        }

        // Show random placeholder image for error state
        if (isError) {
            Image(
                painter = painterResource(id = randomPlaceholderRes),
                contentDescription = "Error Placeholder Image",
                contentScale = scale,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}