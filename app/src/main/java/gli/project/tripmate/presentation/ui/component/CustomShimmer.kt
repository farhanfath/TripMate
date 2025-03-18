package gli.project.tripmate.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.util.ShimmerProvider

@Composable
fun CustomShimmer(modifier: Modifier = Modifier) {
    val brush = ShimmerProvider.getBrush()
    Box(
        modifier = modifier
            .width(80.dp)
            .height(32.dp)
            .clip(MaterialTheme.shapes.small)
            .background(brush)
    )
}