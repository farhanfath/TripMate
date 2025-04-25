package gli.project.tripmate.presentation.ui.screen.call

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gli.project.tripmate.data.helper.agora.AgoraVoiceCallManager
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

@Composable
fun ActiveCallScreen(
    onEndCallClick: () -> Unit,
    callerName: String = "Customer",
    callerProfilePicUrl: String? = null,
    callStartTime: Long = System.currentTimeMillis()
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var connectionState by remember { mutableStateOf("Connected") }
    val agoraManager by remember { mutableStateOf<AgoraVoiceCallManager?>(null) }

    // Connection simulation (simplified)
    LaunchedEffect(key1 = Unit) {
        // Occasional connection status updates (very lightweight)
        while (true) {
            delay(15000) // Check every 15 seconds
            val randomValue = Random.nextFloat()
            if (randomValue < 0.1f) { // 10% chance of reconnection event
                connectionState = "Reconnecting..."
                delay(2000)
                connectionState = "Connected"
            }
        }
    }

    // Simple background colors
    val backgroundColor = MaterialTheme.colorScheme.surface

    // Call timer
    var callDuration by remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = Unit) {
        while (true) {
            callDuration = ((System.currentTimeMillis() - callStartTime) / 1000).toInt()
            delay(1000)
        }
    }

    // Format call duration
    val formattedDuration = remember(callDuration) {
        val minutes = callDuration / 60
        val seconds = callDuration % 60
        String.format(Locale("id", "ID"),"%02d:%02d", minutes, seconds)
    }

    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // Simple status bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Connection indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = when (connectionState) {
                                    "Connected" -> Color.Green
                                    else -> Color.Yellow
                                },
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = connectionState,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Call duration
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedDuration,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Call info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = callerName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ongoing call",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            // Caller avatar with minimal effects
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Simplified audio indicator
                    SimplifiedAudioBars(
                        modifier = Modifier
                            .width(120.dp)
                            .height(30.dp),
                        isActive = !isMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            )
                            .border(
                                width = 2.dp,
                                color = if (connectionState == "Connected")
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Yellow,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (callerProfilePicUrl != null) {
                            CustomImageLoader(
                                url = callerProfilePicUrl,
                                desc = "",
                                scale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Headset,
                                contentDescription = "Customer Service",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        // Simple mute indicator
                        if (isMuted) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(32.dp)
                                    .background(Color.Red, CircleShape)
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MicOff,
                                    contentDescription = "Muted",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simple connection status message
                    if (connectionState == "Reconnecting...") {
                        Text(
                            text = "Reconnecting to customer service...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Yellow
                        )
                    }

                    // Simple activity indicator (lightweight)
                    if (callDuration % 10 > 7) { // Show occasionally
                        Text(
                            text = "Customer service is active",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Call controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Call buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Mute button
                    SimpleCallButton(
                        icon = if (isMuted) Icons.Filled.MicOff else Icons.Filled.Mic,
                        label = if (isMuted) "Unmute" else "Mute",
                        isActive = isMuted,
                        onClick = {
                            isMuted = !isMuted
                            agoraManager?.toggleMute(isMuted)
                        }
                    )

                    // End call button
                    SimpleCallButton(
                        icon = Icons.Filled.CallEnd,
                        label = "End Call",
                        isActive = true,
                        onClick = {
                            agoraManager?.cleanup()
                            onEndCallClick()
                        },
                        backgroundColor = Color.Red,
                        iconColor = Color.White
                    )

                    // Speaker button
                    SimpleCallButton(
                        icon = if (isSpeakerOn) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.Phone,
                        label = if (isSpeakerOn) "Speaker" else "Phone",
                        isActive = isSpeakerOn,
                        onClick = {
                            isSpeakerOn = !isSpeakerOn
                            agoraManager?.enableSpeakerphone(isSpeakerOn)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SimplifiedAudioBars(
    modifier: Modifier = Modifier,
    isActive: Boolean = true
) {
    val barCount = 5
    val infiniteTransition = rememberInfiniteTransition(label = "audioBar")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(barCount) { index ->
            val height by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = if (isActive) {
                    when (index) {
                        0, 4 -> 0.5f
                        1, 3 -> 0.7f
                        else -> 0.9f
                    }
                } else 0.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 700,
                        delayMillis = index * 100,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar$index"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(height)
                    .padding(horizontal = 2.dp)
                    .background(
                        color = if (isActive)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}

@Composable
fun SimpleCallButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color = if (isActive)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface,
    iconColor: Color = if (isActive)
        Color.White
    else
        MaterialTheme.colorScheme.onSurface
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = backgroundColor,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
