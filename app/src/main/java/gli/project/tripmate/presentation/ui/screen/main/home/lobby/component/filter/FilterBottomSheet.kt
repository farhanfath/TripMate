package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gli.project.tripmate.domain.util.constants.HOTEL
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.filter.component.CategoryFilterSection
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun FilterBottomSheet(
    onConfirm: (filterName: String, filterEndpoint: String) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    placesViewModel: PlacesViewModel
) {
    var fixSelectedFilterName by remember { mutableStateOf("Hotels") }
    var fixSelectedFilterEndpoint by remember { mutableStateOf(HOTEL) }

    BaseModalBottomSheet(
        modifier = Modifier.padding(top = 150.dp),
        isVisible = isVisible,
        onDismiss = onDismiss,
        fullScreen = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // title filter
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        text = "Filter Lokasi",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    HorizontalDivider()
                }

                CategoryFilterSection(
                    onFilterSelected = { filterName, filterEndpoint ->
                        fixSelectedFilterName = filterName
                        fixSelectedFilterEndpoint = filterEndpoint
                    },
                    placesViewModel = placesViewModel
                )
            }

            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp,

                ),
                onClick = {
                    onConfirm(
                        fixSelectedFilterName,
                        fixSelectedFilterEndpoint
                    )
                }
            ) {
                Text(
                    text = "Tampilkan Hasil Pencarian"
                )
            }
        }
    }
}