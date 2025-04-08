package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.filter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gli.project.tripmate.domain.model.PlaceCategory
import gli.project.tripmate.domain.util.constants.DataConstants
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import kotlinx.coroutines.flow.map

@Composable
fun CategoryFilterSection(
    onFilterSelected: (filterName: String, filterEndpoint: String) -> Unit,
    placesViewModel: PlacesViewModel
) {
    val filters by placesViewModel.placesState.map { it.placesCategory }.collectAsState(initial = DataConstants.placeFilterCategories)
    var selectedFilter by remember { mutableStateOf(filters.first()) }

    CategoryFilterGroup(
        filters = filters,
        selectedFilter = selectedFilter.name,
        onFilterSelected = { selectedName ->
            // update the selected UI
            selectedFilter = filters.find {
                it.name == selectedName
            } ?: PlaceCategory(selectedFilter.name, selectedFilter.categoryEndpoint, selectedFilter.icon)

            // for updating the endpoint for change in places api
            onFilterSelected(selectedFilter.name, selectedFilter.categoryEndpoint)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryFilterGroup(
    filters: List<PlaceCategory>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.padding(20.dp)
    ) {
        item {
            FlowRow(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = filter.name == selectedFilter
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { onFilterSelected(filter.name) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = filter.icon,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = filter.name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}