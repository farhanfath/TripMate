package gli.project.tripmate.presentation.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.*

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    dashWidth: Dp = 8.dp,
    dashGap: Dp = 4.dp
) {
    Canvas(
        modifier = modifier
            .height(thickness)
    ) {
        val dashWidthPx = dashWidth.toPx()
        val dashGapPx = dashGap.toPx()
        val totalWidth = size.width

        var startX = 0f
        val path = Path()

        while (startX < totalWidth) {
            path.moveTo(startX, 0f)
            path.lineTo(startX + dashWidthPx, 0f)
            startX += dashWidthPx + dashGapPx
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(thickness.toPx())
        )
    }
}