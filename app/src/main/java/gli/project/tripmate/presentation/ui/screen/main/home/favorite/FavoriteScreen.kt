package gli.project.tripmate.presentation.ui.screen.main.home.favorite

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.component.common.CustomTopBarWithNavigation
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.EmptyFavoriteState
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.ErrorState
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.FavoriteGridItem
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.SwipeableFavoriteItem
import gli.project.tripmate.presentation.viewmodel.main.FavoriteViewModel

@Composable
fun FavoriteScreen(
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onBackClick: () -> Unit,
    onExploreClick: () -> Unit
) {
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    val favoritePlaces = favoriteViewModel.getFavoritePlaces().collectAsLazyPagingItems()

    // State for layout type (list or grid)
    var isGridLayout by remember { mutableStateOf(false) }

    // Animation for layout change
    val transition = updateTransition(isGridLayout, label = "layoutTransition")
    val contentPadding by transition.animateDp(label = "contentPadding") { grid ->
        if (grid) 12.dp else 8.dp
    }

    // State for current category filter
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = remember { listOf("All", "Beach", "Mountain", "City", "Cultural") }

    Scaffold(
        topBar = {
            Column {
                FavoriteTopBar(
                    title = "My Travel Collection",
                    onBackClick = onBackClick,
                    isGridLayout = isGridLayout,
                    onLayoutToggle = { isGridLayout = !isGridLayout }
                )

                // Horizontal scrolling category chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            selected = (category == "All" && selectedCategory == null) || category == selectedCategory,
                            onClick = {
                                selectedCategory = if (category == "All") null else category
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                favoritePlaces.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                favoritePlaces.loadState.refresh is LoadState.Error -> {
                    ErrorState(
                        modifier = Modifier.fillMaxSize(),
                        onRetry = { favoritePlaces.refresh() }
                    )
                }
                favoritePlaces.itemCount == 0 -> {
                    EmptyFavoriteState(
                        modifier = Modifier.padding(vertical = 200.dp),
                        onExploreClick = onExploreClick
                    )
                }
                else -> {
                    if (isGridLayout) {
                        // Grid layout
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(contentPadding),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favoritePlaces.itemCount) { index ->
                                favoritePlaces[index]?.let { placeFavData ->
                                    FavoriteGridItem(
                                        favPlace = placeFavData,
                                        onDetailClick = onDetailClick,
                                        onDelete = {
                                            favoriteViewModel.removeFavoriteById(placeFavData.favoriteId)
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        // List layout with swipeable items
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(vertical = contentPadding),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                count = favoritePlaces.itemCount,
                                key = { index -> favoritePlaces[index]?.favoriteId ?: index }
                            ) { index ->
                                favoritePlaces[index]?.let { placeFavData ->
                                    SwipeableFavoriteItem(
                                        onDetailClick = onDetailClick,
                                        onDelete = {
                                            favoriteViewModel.removeFavoriteById(placeFavData.favoriteId)
                                        },
                                        favPlace = placeFavData
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Floating action button for sorting or other actions
            FloatingActionButton(
                onClick = { /* Implement sorting or other actions */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Sort,
                    contentDescription = "Sort",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteTopBar(
    title: String,
    onBackClick: () -> Unit,
    isGridLayout: Boolean,
    onLayoutToggle: () -> Unit
) {

    CustomTopBar(
        additionalContent = {
            Surface(
                color = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(5f)
                    )
                    IconButton(
                        modifier = Modifier.weight(1f),
                        onClick = onLayoutToggle
                    ) {
                        Icon(
                            imageVector = if (isGridLayout) Icons.AutoMirrored.Default.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridLayout) "List View" else "Grid View"
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryChip(
    category: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(32.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}