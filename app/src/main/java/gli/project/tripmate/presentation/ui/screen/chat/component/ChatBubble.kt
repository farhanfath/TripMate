package gli.project.tripmate.presentation.ui.screen.chat.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.util.extensions.parseMarkdown
import gli.project.tripmate.presentation.viewmodel.Message
import kotlinx.coroutines.delay

@Composable
fun ChatBubble(message: Message) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = if (message.isUser) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    },
                    shape = if (message.isUser) {
                        RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp)
                    } else {
                        RoundedCornerShape(topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp)
                    }
                )
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            if (message.isUser) {
                Text(
                    text = parseMarkdown(message.text),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                AnimatedTypingText(message.text)
            }
        }
    }
}

@Composable
fun AnimatedTypingText(fullText: String) {
    // State to track the progress of typing animation
    var textProgress by remember { mutableStateOf(0f) }

    // Determine the text color
    val textColor = MaterialTheme.colorScheme.onSurface

    // Text that will be displayed gradually
    val visibleText = remember(fullText, textProgress) {
        val endIndex = (fullText.length * textProgress).toInt()
        fullText.take(endIndex)
    }

    // Display the text that has been "typed" so far
    val displayText = remember(visibleText, textProgress) {
        if (textProgress >= 1f) {
            // If animation is complete, just show the full text with proper markdown
            parseMarkdown(fullText)
        } else {
            // During animation, only show the visible part with proper markdown
            buildAnnotatedString {
                // Already typed text with proper styling
                append(parseMarkdown(visibleText))

                // Text to be typed (we'll show the raw text, not parsed)
                val remainingText = fullText.substring(visibleText.length)

                // We'll use a simpler approach - instead of having faded future text,
                // we'll just show a blinking cursor
                if (textProgress < 1f) {
                    append("▋") // Cursor character
                }
            }
        }
    }

    // Configuration for typing speed
    val typingSpeed = 40 // Characters per second
    val animationDuration = (fullText.length * 1000 / typingSpeed).coerceAtLeast(300) // ms

    // Start the animation when the composable enters the composition
    LaunchedEffect(fullText) {
        textProgress = 0f
        // Small delay before typing starts
        delay(300)

        // Animate from 0 to 1 for smooth typing effect
        val startTime = System.currentTimeMillis()
        val endTime = startTime + animationDuration

        while (System.currentTimeMillis() < endTime) {
            val elapsed = System.currentTimeMillis() - startTime
            textProgress = (elapsed.toFloat() / animationDuration).coerceIn(0f, 1f)
            delay(16) // Approximately 60fps
        }

        textProgress = 1f
    }

    Text(
        text = displayText,
        color = textColor
    )
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    Column {
        ChatBubble(
            message = Message(
                text = "Hello world",
                isUser = true
            )
        )
        ChatBubble(
            message = Message(
                text = "Hello world",
                isUser = false
            )
        )
    }
}

@Composable
fun PulsatingDots() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PulsatingDot(delay = 0)
        PulsatingDot(delay = 150)
        PulsatingDot(delay = 300)
    }
}

@Composable
fun PulsatingDot(delay: Int = 0) {
    // Infinite transition untuk animasi yang berjalan terus menerus
    val infiniteTransition = rememberInfiniteTransition(label = "pulsating")

    // Animasi scale dari 0.5 ke 1.0 dan kembali lagi
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                delayMillis = delay
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Surface dengan ukuran yang dianimasikan
    Box(
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = CircleShape,
            modifier = Modifier.size(4.dp + (12.dp * scale))
        ) {}
    }
}