package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.filter.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingFilterSection() {
    BaseFilterSection(
        filterTitle = "Rating Tempat"
    ) {
        val selectedRatings = remember {
            mutableStateListOf<Int>()
        }
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) { index ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = selectedRatings.contains(index + 1),
                    onClick = {
                        val rating = index + 1
                        if (selectedRatings.contains(rating)) {
                            selectedRatings.remove(rating)
                        } else {
                            selectedRatings.add(rating)
                        }
                    },
                    label = { Text("${index + 1}") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating ${index + 1}",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                )
            }
        }
    }
}