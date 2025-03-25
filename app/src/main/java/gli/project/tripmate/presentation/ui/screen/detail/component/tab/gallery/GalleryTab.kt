package gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component.GalleryRowGrid
import gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component.MoreGalleryBottomSheet
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun GalleryTab(
    viewModel: PlacesViewModel,
    detailPlaceName: String
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val placeImageList = viewModel.getDetailListImage(detailPlaceName).collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Discover $detailPlaceName in Pictures",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 10.dp, top = 20.dp, bottom = 10.dp)
        )
        GalleryRowGrid(
            imageList = placeImageList,
            onShowMore = { showBottomSheet = true }
        )
    }

    /**
     * bottom sheet handler for more gallery
     */
    MoreGalleryBottomSheet(
        isVisible = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        placeImageList = placeImageList,
    )
}