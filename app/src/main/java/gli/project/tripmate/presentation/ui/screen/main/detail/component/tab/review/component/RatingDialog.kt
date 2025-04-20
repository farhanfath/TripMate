package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.review.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun RatingDialog(
    showDialog: Boolean = true,
    onDismiss: () -> Unit = {},
    onRatingSubmitted: (rating: Double, description: String) -> Unit = { rating, description -> }
) {
    if (showDialog) {
        var selectedRating by remember { mutableStateOf<RatingValue?>(null) }
        var ratingDescription by remember { mutableStateOf("") }
        var isSubmitting by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dialog Title with animated stars
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Rate Your Experience",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Rating Options
                    RatingOptions(
                        selectedRating = selectedRating,
                        onRatingSelected = { rating ->
                            selectedRating = rating
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description field
                    OutlinedTextField(
                        value = ratingDescription,
                        onValueChange = { ratingDescription = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        label = {
                            Text(
                                text = "Tell us about your experience",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        placeholder = {
                            Text(
                                text = "What did you like or dislike?",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                selectedRating?.let { rating ->
                                    isSubmitting = true
                                    scope.launch {
                                        // Convert rating to numeric value
                                        val numericRating = when(rating) {
                                            RatingValue.EXCELLENT -> 5.0
                                            RatingValue.VERY_GOOD -> 4.0
                                            RatingValue.GOOD -> 3.0
                                            RatingValue.AVERAGE -> 2.0
                                            RatingValue.POOR -> 1.0
                                        }

                                        // Simulate submission with delay
                                        delay(500)
                                        onRatingSubmitted(numericRating, ratingDescription)
                                        isSubmitting = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = selectedRating != null && !isSubmitting
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class RatingValue(val value: String, val emoji: String) {
    POOR("Poor", "😔"),
    AVERAGE("Average", "😐"),
    GOOD("Good", "🙂"),
    VERY_GOOD("Very Good", "😊"),
    EXCELLENT("Excellent", "🤩")
}

@Composable
fun RatingOptions(
    selectedRating: RatingValue?,
    onRatingSelected: (RatingValue) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            RatingValue.entries.forEachIndexed { index, rating ->
                val isSelected = selectedRating?.ordinal?.let { index <= it } ?: false

                val transition =
                    updateTransition(targetState = isSelected, label = "star_transition")
                val scale by transition.animateFloat(
                    label = "star_scale",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
                ) { filled -> if (filled) 1.2f else 1f }

                val starColor by transition.animateColor(
                    label = "star_color",
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
                ) { filled -> if (filled) Color(0xFFFFD700) else Color.Gray.copy(alpha = 0.5f) }

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = starColor,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                        .scale(scale)
                        .clickable {
                            onRatingSelected(rating)
                        }
                )
            }
        }

        selectedRating?.let { rating ->
            Spacer(modifier = Modifier.height(8.dp))

            val feedbackMessage = when(rating) {
                RatingValue.POOR -> "We're sorry you didn't have a better experience."
                RatingValue.AVERAGE -> "Thank you for your honest feedback."
                RatingValue.GOOD -> "We're glad you had a good experience."
                RatingValue.VERY_GOOD -> "Thank you for your very positive feedback!"
                RatingValue.EXCELLENT -> "We're thrilled you had an excellent experience!"
            }

            Text(
                text = feedbackMessage,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        }
    }
}
