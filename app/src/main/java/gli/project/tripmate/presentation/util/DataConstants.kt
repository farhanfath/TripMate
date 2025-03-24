package gli.project.tripmate.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.LocalAirport
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

object DataConstants {
    val tabName = listOf(
        "About",
        "Gallery",
        "Review"
    )

    val featureCategory = listOf(
        FeatureCategory(name = "Hotels", icon = Icons.Default.Bed),
        FeatureCategory(name = "Restaurant", icon = Icons.Default.Restaurant),
        FeatureCategory(name = "Mall", icon = Icons.Default.LocalMall),
        FeatureCategory(name = "Airport", icon = Icons.Default.LocalAirport),
    )
}

data class FeatureCategory(
    val name: String,
    val icon: ImageVector
)