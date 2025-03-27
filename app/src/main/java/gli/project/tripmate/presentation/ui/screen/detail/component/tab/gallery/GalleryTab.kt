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
import androidx.compose.ui.tooling.preview.Preview
import gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component.GalleryRowGrid
import gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component.MoreGalleryBottomSheet
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_20

@Composable
fun GalleryTab() {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding_20)
    ) {
        repeat(2) {
            Text(
                text = "Sample Gallery",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = padding_10, top = padding_20, bottom = padding_10)
            )
            GalleryRowGrid(
                onShowMore = { showBottomSheet = true }
            )
        }
    }

    /**
     * bottom sheet handler for more gallery
     */
    MoreGalleryBottomSheet(
        isVisible = showBottomSheet,
        onDismiss = { showBottomSheet = false }
    )
}

@Preview(showBackground = true)
@Composable
fun GalleryTabPreview() {
    GalleryTab()
}