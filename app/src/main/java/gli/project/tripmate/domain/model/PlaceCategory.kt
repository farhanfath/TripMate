package gli.project.tripmate.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class PlaceCategory(
    val name: String,
    val categoryEndpoint: String,
    val icon: ImageVector
)