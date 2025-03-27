package gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gli.project.tripmate.presentation.ui.component.BaseModalBottomSheet
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_8

@Composable
fun MoreGalleryBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    BaseModalBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        val items = generateStoreItems(50) // Buat 20 item toko

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding_8)
        ) {
            // Proses item-item dalam grup tiga (1 besar + 2 kecil)
            items.chunked(3).forEachIndexed { _, group ->
                item {
                    // 1 item besar (full width)
                    if (group.isNotEmpty()) {
                        ImageCard(
                            item = group[0],
                            isBig = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // 2 item kecil dalam satu baris (jika ada)
                    if (group.size > 1) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (i in 1 until minOf(3, group.size)) {
                                ImageCard(
                                    item = group[i],
                                    isBig = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Spacer jika hanya ada 1 item kecil
                            if (group.size == 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // Spacer antara grup
                    Spacer(modifier = Modifier.height(size_8))
                }
            }
        }
    }
}