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

    // endpoint category
    private const val HOTEL = "accommodation.hotel"
    private const val RESTAURANT = "catering.restaurant"
    private const val MALL = "commercial.shopping_mall"
    private const val AIRPORT = "airport"

    val featureCategory = listOf(
        FeatureCategory(name = "Hotels", categoryEndpoint = HOTEL, icon = Icons.Default.Bed),
        FeatureCategory(name = "Restaurant", categoryEndpoint = RESTAURANT, icon = Icons.Default.Restaurant),
        FeatureCategory(name = "Mall", categoryEndpoint = MALL, icon = Icons.Default.LocalMall),
        FeatureCategory(name = "Airport", categoryEndpoint = AIRPORT, icon = Icons.Default.LocalAirport),
    )
}

data class FeatureCategory(
    val name: String,
    val categoryEndpoint: String,
    val icon: ImageVector
)