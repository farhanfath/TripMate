package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gli.project.tripmate.domain.model.PlaceCategory
import gli.project.tripmate.domain.util.constants.DataConstants
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_5

@Composable
fun CategorySection(
    onCategoryDetailClick: (name: String, endpoint: String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = padding_5, horizontal = padding_10)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val categories = DataConstants.placeCategories
        categories.forEach { category ->
            CategoryItem(
                category = category,
                onCategoryDetailClick = {
                    onCategoryDetailClick(category.name, category.categoryEndpoint)
                }
            )
        }
    }
}

@Composable
fun RowScope.CategoryItem(
    category: PlaceCategory,
    onCategoryDetailClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .size(70.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 20f
            }
            .weight(1f)
            .clickable {
                onCategoryDetailClick()
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = "",
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}