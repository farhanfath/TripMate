package gli.project.tripmate.presentation.ui.screen.main.home.favorite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.EmptyFavoriteState
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.ErrorState
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.SwipeableFavoriteItem
import gli.project.tripmate.presentation.viewmodel.main.FavoriteViewModel

@Composable
fun FavoriteScreen(
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    val favoriteViewModel : FavoriteViewModel = hiltViewModel()

    val favoritePlaces = favoriteViewModel.getFavoritePlaces().collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CustomTopBar()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            when {
                favoritePlaces.loadState.refresh is LoadState.Loading -> {

                }
                favoritePlaces.loadState.refresh is LoadState.Error -> {
                    item {
                        ErrorState(
                            modifier = Modifier.fillParentMaxSize(),
                            onRetry = { favoritePlaces.refresh() }
                        )
                    }
                }
                favoritePlaces.itemCount == 0 -> {
                    item {
                        EmptyFavoriteState(
                            modifier = Modifier.padding(vertical = 200.dp)
                        )
                    }
                }
                else -> {
                    items(favoritePlaces.itemCount) { index ->
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
}