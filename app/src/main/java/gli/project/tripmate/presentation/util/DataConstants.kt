package gli.project.tripmate.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHotel
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
    private const val CAFE = "catering.cafe"

    val featureCategory = listOf(
        FeatureCategory(name = "Hotels", categoryEndpoint = HOTEL, icon = Icons.Default.LocalHotel),
        FeatureCategory(name = "Restaurant", categoryEndpoint = RESTAURANT, icon = Icons.Default.Restaurant),
        FeatureCategory(name = "Mall", categoryEndpoint = MALL, icon = Icons.Default.LocalMall),
        FeatureCategory(name = "Cafe", categoryEndpoint = CAFE, icon = Icons.Default.LocalCafe),
    )
}

data class FeatureCategory(
    val name: String,
    val categoryEndpoint: String,
    val icon: ImageVector
)