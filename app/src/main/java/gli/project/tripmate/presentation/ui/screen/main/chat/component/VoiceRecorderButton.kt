package gli.project.tripmate.presentation.ui.screen.main.chat.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VoiceRecorderButton(
    isListening: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val buttonSize by animateFloatAsState(
        targetValue = if (isListening) 1.2f else 1f,
        label = "buttonSize"
    )

    Icon(
        modifier = Modifier
            .size((32 * buttonSize).dp)
            .clickable { onClick() },
        imageVector = when {
            isLoading -> Icons.Default.Refresh
            isListening -> Icons.Filled.MicOff
            else -> Icons.Default.Mic
        },
        contentDescription = if (isListening) "Stop Recording" else "Start Recording",
        tint = if (isListening) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
    )
}