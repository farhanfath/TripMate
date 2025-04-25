package gli.project.tripmate.presentation.ui.screen.main.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.PlaceCategory
import gli.project.tripmate.domain.util.constants.DataConstants
import gli.project.tripmate.domain.util.constants.TOURISM
import gli.project.tripmate.presentation.ui.component.common.CustomShimmer
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.screen.main.category.component.NearbyPlaceLongItem
import gli.project.tripmate.presentation.ui.screen.main.search.component.CategorySearchGroup
import gli.project.tripmate.presentation.ui.screen.main.search.component.ListSearchBottomSheet
import gli.project.tripmate.presentation.util.extensions.HandleComposePagingState
import gli.project.tripmate.presentation.util.extensions.handlePagingAppendState
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalLayoutApi::class)
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

    // Animation state untuk search
    val searchBarElevation by animateDpAsState(
        targetValue = if (searchQuery.isNotEmpty()) 8.dp else 4.dp,
        label = "searchElevation"
    )

    // Background gradient
    val gradientColors = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    )

    Scaffold(
        modifier = Modifier.imeNestedScroll(),
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = gradientColors)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                // search bar with enhanced appearance
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = searchBarElevation, shape = CircleShape)
                        .animateContentSize(),
                    placeholder = {
                        Text(
                            "Search places...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp)
                                .clickable(
                                    onClick = { onBackClick() }
                                ),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty(),
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            IconButton(
                                onClick = {
                                    placesViewModel.updateSearchQuery(searchQuery)
                                    placesViewModel.setCategory(searchCategory)
                                    placesViewModel.triggerSearch()
                                    showResultsBottomSheet = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            placesViewModel.updateSearchQuery(searchQuery)
                            placesViewModel.setCategory(searchCategory)
                            placesViewModel.triggerSearch()
                            showResultsBottomSheet = true
                        }
                    ),
                    shape = CircleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // category header with animation
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Categories",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced category selection
                AnimatedCategorySearchGroup(
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

                // Popular searches or recent searches section (optional)
                if (searchQuery.isEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    PopularSearchesSection()
                }
            }

            // Bottom sheet with improved appearance
            ListSearchBottomSheet(
                onDetailClick = onDetailClick,
                onDismiss = { showResultsBottomSheet = false },
                isVisible = showResultsBottomSheet,
                placesViewModel = placesViewModel
            )
        }
    }
}

@Composable
fun AnimatedCategorySearchGroup(
    categories: List<PlaceCategory>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.name == selectedCategory
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                label = "bgColor"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                label = "contentColor"
            )

            Card(
                modifier = Modifier
                    .animateContentSize()
                    .clickable { onCategorySelected(category.name) },
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isSelected) 4.dp else 1.dp
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = category.name,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PopularSearchesSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Destinations",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            TextButton(onClick = {}) {
                Text("See All")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                PopularDestinationCard(
                    name = "Popular Place ${index + 1}",
                    imageRes = R.drawable.placeholder_mall // Ganti dengan resource gambar yang sesuai
                )
            }
        }
    }
}

@Composable
fun PopularDestinationCard(name: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = 300f
                        )
                    )
            )

            Text(
                text = name,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedListSearchBottomSheet(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    placesViewModel: PlacesViewModel
) {
    val placesByArea = placesViewModel.searchResults.collectAsLazyPagingItems()
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Search Results",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        ) {
            HandleComposePagingState(
                items = placesByArea,
                onLoading = {
                    LazyColumn {
                        items(10) {
                            CustomShimmer(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                                    .height(200.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                },
                onError = {

                },
                onSuccess = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        items(
                            count = placesByArea.itemCount,
                            key = { index ->
                                val place = placesByArea[index]
                                if (place != null) {
                                    "place_${place.placeId}_$index"
                                } else {
                                    "null_$index"
                                }
                            }
                        ) { index ->
                            placesByArea[index]?.let { place ->
                                NearbyPlaceLongItem(
                                    onDetailClick = {
                                        onDetailClick(place.placeId, place.name)
                                    },
                                    place = place
                                )
                            }
                        }

                        handlePagingAppendState(
                            items = placesByArea,
                            onLoading = {
                                item {
                                    CustomShimmer(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .height(200.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            },
                            onError = {
                                item { Text("Terjadi error saat load awal") }
                            },
                            onNotLoading = {}
                        )
                    }

                }
            )
        }
    }
}

