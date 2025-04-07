package gli.project.tripmate.presentation.ui.screen.main.home.favorite

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.component.SwipeableFavoriteItem
import gli.project.tripmate.presentation.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    onDetailClick: (placeId: String, placeName: String) -> Unit
) {
    val favoriteViewModel : FavoriteViewModel = hiltViewModel()

    val favoritePlaces = favoriteViewModel.getFavoritePlaces().collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        when {
            favoritePlaces.loadState.refresh is LoadState.Loading -> {

            }
            favoritePlaces.loadState.refresh is LoadState.Error -> {

            }
            favoritePlaces.itemCount == 0 -> {

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