package gli.project.tripmate.presentation.ui.screen.main.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gli.project.tripmate.domain.model.PlaceCategory
import gli.project.tripmate.domain.util.constants.DataConstants
import gli.project.tripmate.domain.util.constants.TOURISM
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.screen.main.search.component.CategorySearchGroup
import gli.project.tripmate.presentation.ui.screen.main.search.component.ListSearchBottomSheet
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import kotlinx.coroutines.flow.map

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    placesViewModel: PlacesViewModel,
) {
    var showResultsBottomSheet by rememberSaveable { mutableStateOf(false) }

    // State for search input
    var searchQuery by remember { mutableStateOf("") }
    var searchCategory by remember { mutableStateOf(TOURISM) }

    val categories by placesViewModel.placesState.map { it.placesCategory }.collectAsState(initial = DataConstants.placeFilterCategories)
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text("Search here...") },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            onBackClick()
                        },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )

            // category
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Categories",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            CategorySearchGroup(
                categories = categories,
                selectedCategory = selectedCategory.name,
                onCategorySelected = { selectedName ->
                    selectedCategory = categories.find {
                        it.name == selectedName
                    } ?: PlaceCategory(selectedCategory.name, selectedCategory.categoryEndpoint, selectedCategory.icon)

                    // apply it on variable for search action
                    searchCategory = selectedCategory.categoryEndpoint
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Search button
            Button(
                onClick = {
                    placesViewModel.updateSearchQuery(searchQuery)
                    placesViewModel.setCategory(searchCategory)
                    placesViewModel.triggerSearch()
                    showResultsBottomSheet = true
                },
                enabled = searchQuery.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Search",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        ListSearchBottomSheet(
            onDetailClick = onDetailClick,
            onDismiss = { showResultsBottomSheet = false },
            isVisible = showResultsBottomSheet,
            placesViewModel = placesViewModel
        )
    }
}



