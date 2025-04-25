package gli.project.tripmate.presentation.ui.screen.product.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

@Composable
fun ProductLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    // Animation for cart movement
    val cartPosition by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animation for bouncing products
    val productBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animation for opacity pulse
    val opacity by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val mainColor = MaterialTheme.colorScheme.primary.copy(alpha = opacity)
                val surfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = opacity)
                val tertiaryColor = MaterialTheme.colorScheme.tertiary
                val secondaryColor = MaterialTheme.colorScheme.secondary
                val errorColor = MaterialTheme.colorScheme.error
                Canvas(
                    modifier = Modifier
                        .size(150.dp, 60.dp)
                ) {
                    // Draw cart
                    val cartWidth = 60f
                    val cartHeight = 40f
                    val cartX = size.width / 2 + cartPosition
                    val cartY = size.height - cartHeight / 2



                    // Cart body
                    drawRoundRect(
                        color = mainColor,
                        topLeft = Offset(cartX - cartWidth / 2, cartY - cartHeight / 2),
                        size = Size(cartWidth, cartHeight),
                        cornerRadius = CornerRadius(8f),
                    )

                    // Cart handle
                    drawLine(
                        color = mainColor,
                        start = Offset(cartX + cartWidth / 2, cartY - cartHeight / 4),
                        end = Offset(cartX + cartWidth / 2 + 20f, cartY - cartHeight / 4),
                        strokeWidth = 5f
                    )

                    // Cart wheels
                    drawCircle(
                        color = surfaceVariant,
                        radius = 5f,
                        center = Offset(cartX - cartWidth / 3, cartY + cartHeight / 2 + 5f)
                    )
                    drawCircle(
                        color = surfaceVariant,
                        radius = 5f,
                        center = Offset(cartX + cartWidth / 3, cartY + cartHeight / 2 + 5f)
                    )

                    // Products in cart (bouncing)
                    val productColors = listOf(
                        tertiaryColor,
                        secondaryColor,
                        errorColor
                    )

                    // Draw 3 different sized and colored products
                    drawCircle(
                        color = productColors[0].copy(alpha = opacity),
                        radius = 8f,
                        center = Offset(cartX - 15f, cartY - 10f - productBounce)
                    )
                    drawRect(
                        color = productColors[1].copy(alpha = opacity),
                        topLeft = Offset(cartX - 5f, cartY - 15f - productBounce * 0.7f),
                        size = Size(10f, 10f)
                    )
                    drawRoundRect(
                        color = productColors[2].copy(alpha = opacity),
                        topLeft = Offset(cartX + 10f, cartY - 12f - productBounce * 1.3f),
                        size = Size(12f, 8f),
                        cornerRadius = CornerRadius(2f)
                    )
                }

                Text(
                    text = "Mencari produk terbaik...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                )
            }
        }
    }
}