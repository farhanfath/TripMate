package gli.project.tripmate.presentation.ui.screen.call

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.data.helper.agora.AgoraVoiceCallManager
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ActiveCallScreen(
    onEndCallClick: () -> Unit,
    callerName: String = "Customer",
    callerProfilePicUrl: String? = null,
    callStartTime: Long = System.currentTimeMillis()
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    val agoraManager by remember { mutableStateOf<AgoraVoiceCallManager?>(null) }

    // Background gradient
    val gradientColors = listOf(
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primaryContainer
    )

    // Call timer
    var callDuration by remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = callStartTime) {
        while (true) {
            callDuration = ((System.currentTimeMillis() - callStartTime) / 1000).toInt()
            delay(1000)
        }
    }

    // Format call duration
    val formattedDuration = remember(callDuration) {
        val minutes = callDuration / 60
        val seconds = callDuration % 60
        String.format(Locale("id", "ID"), "%02d:%02d", minutes, seconds)
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
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = callerName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ongoing call • $formattedDuration",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        // Caller avatar in the center
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Pulsating effect
            val infiniteTransition = rememberInfiniteTransition(label = "avatarPulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "avatarScale"
            )

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale)
            ) {
                // Background pulse circles
                repeat(3) { index ->
                    val pulseScale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, delayMillis = index * 600),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "pulse$index"
                    )

                    val pulseAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, delayMillis = index * 600),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "pulseAlpha$index"
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .scale(pulseScale)
                            .alpha(pulseAlpha)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                    )
                }

                // Caller avatar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (callerProfilePicUrl != null) {
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
        }

        // Call actions at the bottom
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Call control buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Mute button
                CallControlButton(
                    icon = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                    label = if (isMuted) "Unmute" else "Mute",
                    backgroundColor = if (isMuted) Color.White else Color.White.copy(alpha = 0.2f),
                    iconTint = if (isMuted) MaterialTheme.colorScheme.primary else Color.White,
                    onClick = {
                        isMuted = !isMuted
                        agoraManager?.toggleMute(isMuted)
                    }
                )

                // End call button
                CallControlButton(
                    icon = Icons.Filled.CallEnd,
                    label = "End",
                    backgroundColor = Color.Red,
                    iconTint = Color.White,
                    onClick = {
                        agoraManager?.cleanup()
                        onEndCallClick()
                    },
                    size = 72.dp
                )

                // Speaker button
                CallControlButton(
                    icon = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.Phone,
                    label = if (isSpeakerOn) "Speaker" else "Phone",
                    backgroundColor = if (isSpeakerOn) Color.White else Color.White.copy(alpha = 0.2f),
                    iconTint = if (isSpeakerOn) MaterialTheme.colorScheme.primary else Color.White,
                    onClick = {
                        isSpeakerOn = !isSpeakerOn
                        agoraManager?.enableSpeakerphone(isSpeakerOn)
                    }
                )
            }
        }
    }
}