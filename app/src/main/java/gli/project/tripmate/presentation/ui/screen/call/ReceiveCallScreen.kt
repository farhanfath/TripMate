package gli.project.tripmate.presentation.ui.screen.call

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IncomingCallScreen(
    onAcceptCall: () -> Unit,
    onDeclineCall: () -> Unit,
    callerName: String = "John Doe",
    callerProfilePicUrl: String? = null
) {
    // Coroutine scope for animations
    val coroutineScope = rememberCoroutineScope()

    // Animation states
    val ringAnimation = remember { Animatable(0f) }

    // Background gradient
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.tertiary
    )

    // Start ring animation
    LaunchedEffect(key1 = true) {
        ringAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(gradientColors))
    ) {
        // Call info at the top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Incoming call text
            Text(
                text = "Incoming Call",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = callerName,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Caller avatar in the center with ringing animation
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Ringing circles animation
            val density = LocalDensity.current
            val canvasSize = with(density) { 300.dp.toPx() }

            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .alpha(0.5f)
            ) {
                val radius = canvasSize / 2
                val ringWidth = 4.dp.toPx()

                for (i in 0 until 3) {
                    val ringProgress = (ringAnimation.value + (i * 0.33f)) % 1f
                    val ringRadius = radius * ringProgress
                    val ringAlpha = 1f - ringProgress

                    drawCircle(
                        color = Color.White.copy(alpha = ringAlpha),
                        radius = ringRadius,
                        center = Offset(radius, radius),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = ringWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            // Caller avatar
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(4.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (callerProfilePicUrl != null) {
//                    AsyncImage(
//                        model = callerProfilePicUrl,
//                        contentDescription = "Caller profile picture",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
                    CustomImageLoader(
                        url = "",
                        desc = "Caller profile picture",
                        scale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Caller",
                        tint = Color.White,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        // Call actions at the bottom (answer/decline)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Decline call button - slide animation
            var declineOffsetY by remember { mutableFloatStateOf(100f) }

            LaunchedEffect(key1 = true) {
                animate(
                    initialValue = 100f,
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 800, easing = EaseOutBack)
                ) { value, _ ->
                    declineOffsetY = value
                }
            }

            Box(
                modifier = Modifier
                    .offset(y = declineOffsetY.dp)
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .clickable {
                        coroutineScope.launch {
                            // Animate decline
                            animate(
                                initialValue = 1f,
                                targetValue = 0f,
                                animationSpec = tween(300)
                            ) { value, _ ->
                                declineOffsetY = 100f * (1f - value)
                            }
                            onDeclineCall()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CallEnd,
                    contentDescription = "Decline call",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Accept call button - slide animation
            var acceptOffsetY by remember { mutableFloatStateOf(100f) }

            LaunchedEffect(key1 = true) {
                delay(200)
                animate(
                    initialValue = 100f,
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 800, easing = EaseOutBack)
                ) { value, _ ->
                    acceptOffsetY = value
                }
            }

            Box(
                modifier = Modifier
                    .offset(y = acceptOffsetY.dp)
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                    .clickable {
                        coroutineScope.launch {
                            // Animate accept
                            animate(
                                initialValue = 1f,
                                targetValue = 0f,
                                animationSpec = tween(300)
                            ) { value, _ ->
                                acceptOffsetY = 100f * (1f - value)
                            }
                            onAcceptCall()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Accept call",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Swipe to answer text with animation
        val pulseAnimation = rememberInfiniteTransition(label = "pulseAnimation")
        val textAlpha by pulseAnimation.animateFloat(
            initialValue = 0.6f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "textAlpha"
        )

        Text(
            text = "Swipe to answer",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = textAlpha),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}