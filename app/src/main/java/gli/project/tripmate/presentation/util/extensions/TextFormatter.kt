package gli.project.tripmate.presentation.util.extensions

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import gli.project.tripmate.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun locationRangeFormat(range: Double) : String {
    return String.format(Locale("id", "ID"), "%.1f", range) + " km dari Lokasi anda"
}

fun formatOperatingHours(input: String?): String {
    val daysMap = mapOf(
        "Mo" to "Senin",
        "Tu" to "Selasa",
        "We" to "Rabu",
        "Th" to "Kamis",
        "Fr" to "Jumat",
        "Sa" to "Sabtu",
        "Su" to "Minggu"
    )

    val regex = Regex("([A-Za-z]{2})-([A-Za-z]{2}) (\\d{2}:\\d{2})-(\\d{2}:\\d{2})")
    val matchResult = input?.let { regex.find(it) }

    return if (matchResult != null) {
        val (startDay, endDay, openTime, closeTime) = matchResult.destructured
        val formattedStartDay = daysMap[startDay] ?: startDay
        val formattedEndDay = daysMap[endDay] ?: endDay
        "$formattedStartDay - $formattedEndDay, Pukul $openTime - $closeTime"
    } else {
        "Informasi jam buka belum tersedia"
    }
}

fun emptyTextHandler(original: String, placeHolder: String) : String {
    return original.ifEmpty {
        placeHolder
    }
}

fun Long.formatToRelativeTime(context: Context): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < 1000 * 60 -> context.getString(R.string.just_now)
        diff < 1000 * 60 * 60 -> context.getString(R.string.minutes_ago, diff / (1000 * 60))
        diff < 1000 * 60 * 60 * 24 -> context.getString(R.string.hours_ago, diff / (1000 * 60 * 60))
        diff < 1000 * 60 * 60 * 24 * 7 -> context.getString(R.string.days_ago, diff / (1000 * 60 * 60 * 24))
        else -> {
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            formatter.format(Date(this))
        }
    }
}