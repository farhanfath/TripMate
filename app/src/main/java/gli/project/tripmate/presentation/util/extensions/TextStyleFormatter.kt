package gli.project.tripmate.presentation.util.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

// Fungsi untuk parsing markdown (sama seperti sebelumnya)
fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        val currentText = StringBuilder()
        var index = 0

        // Status tracking
        var isBold = false
        var isItalic = false
        var isCode = false

        while (index < text.length) {
            when {
                // Bold
                text.startsWith("**", index) || text.startsWith("__", index) -> {
                    // Append accumulated text with current styling
                    if (currentText.isNotEmpty()) {
                        val style = SpanStyle(
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default
                        )
                        pushStyle(style)
                        append(currentText.toString())
                        pop()
                        currentText.clear()
                    }

                    isBold = !isBold
                    index += 2
                }

                // Italic
                text[index] == '*' || text[index] == '_' -> {
                    // Append accumulated text with current styling
                    if (currentText.isNotEmpty()) {
                        val style = SpanStyle(
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default
                        )
                        pushStyle(style)
                        append(currentText.toString())
                        pop()
                        currentText.clear()
                    }

                    isItalic = !isItalic
                    index++
                }

                // Inline code
                text[index] == '`' -> {
                    // Append accumulated text with current styling
                    if (currentText.isNotEmpty()) {
                        val style = SpanStyle(
                            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                            fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default
                        )
                        pushStyle(style)
                        append(currentText.toString())
                        pop()
                        currentText.clear()
                    }

                    isCode = !isCode
                    index++
                }

                else -> {
                    currentText.append(text[index])
                    index++
                }
            }
        }

        // Append any remaining text
        if (currentText.isNotEmpty()) {
            val style = SpanStyle(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default
            )
            pushStyle(style)
            append(currentText.toString())
            pop()
        }
    }
}

@Composable
fun AnnotatedString.Builder.applyCurrentStyling(
    text: String,
    isBold: Boolean,
    isItalic: Boolean,
    isCode: Boolean
): AnnotatedString {
    if (text.isEmpty()) return AnnotatedString("")

    return buildAnnotatedString {
        val style = SpanStyle(
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            fontFamily = if (isCode) FontFamily.Monospace else FontFamily.Default,
            background = if (isCode) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color.Unspecified
        )

        pushStyle(style)
        append(text)
        pop()
    }
}