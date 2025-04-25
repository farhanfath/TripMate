package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun MapLoadingAnimation(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF2F2F2),
            )
    ) {
        // Peta background
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Garis-garis peta
            val lineColor = Color(0xFFE0E0E0)
            val horizontalLines = 8
            val verticalLines = 6

            val horizontalSpacing = size.height / horizontalLines
            val verticalSpacing = size.width / verticalLines

            // Draw horizontal lines
            for (i in 0..horizontalLines) {
                val y = i * horizontalSpacing
                drawLine(
                    color = lineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw vertical lines
            for (i in 0..verticalLines) {
                val x = i * verticalSpacing
                drawLine(
                    color = lineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw few random circles as locations
            val random = Random(42) // Fixed seed for consistency
            repeat(5) {
                val x = random.nextFloat() * size.width
                val y = random.nextFloat() * size.height
                drawCircle(
                    color = Color(0xFFDDDDDD),
                    radius = 6.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        // Pin animasi
        PulsingLocationPin(
            modifier = Modifier
                .align(Alignment.Center)
        )

        // Text loading
        Text(
            text = "Mencari lokasi terdekat...",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PulsingLocationPin(modifier: Modifier = Modifier) {
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val bounceAnim = rememberInfiniteTransition(label = "bounce")
    val offsetY by bounceAnim.animateFloat(
        initialValue = 0f,
        targetValue = (-10f),
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = modifier
            .offset(y = offsetY.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pulse circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(scale)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.3f), CircleShape)
        )

        // Pin
        Box(
            modifier = Modifier
                .offset(y = (-12).dp)
                .size(20.dp)
                .background(color = MaterialTheme.colorScheme.primary, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

// Ganti CustomShimmer dengan animasi peta ini
@Composable
fun NearbyPlacesLoadingItem() {
    MapLoadingAnimation(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .height(200.dp)
            .width(180.dp)
    )
}