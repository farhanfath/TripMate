package gli.project.tripmate.presentation.ui.screen.main.detail.component.tab.gallery.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.presentation.ui.component.common.BaseModalBottomSheet
import gli.project.tripmate.presentation.util.extensions.handlePagingState

@Composable
fun MoreGalleryBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    placeImageList: LazyPagingItems<PexelImage>
) {
    BaseModalBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            handlePagingState(
                items = placeImageList,
                onLoading = {
                    // Loading state handling
                },
                onSuccess = {
                    // Process items in groups of three (1 big + 2 small)
                    val groupCount = (placeImageList.itemCount + 2) / 3 // Ceiling division

                    for (groupIndex in 0 until groupCount) {
                        item(key = "group_$groupIndex") {
                            val startIndex = groupIndex * 3
                            val endIndex = minOf(startIndex + 3, placeImageList.itemCount)

                            // 1 item big (full width)
                            if (startIndex < placeImageList.itemCount) {
                                val imageData = placeImageList[startIndex]
                                if (imageData != null) {
                                    ImageCard(
                                        imageData = imageData,
                                        isBig = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // 2 items small in one row (if available)
                            if (startIndex + 1 < placeImageList.itemCount) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    for (i in startIndex + 1 until endIndex) {
                                        val imageData = placeImageList[i]
                                        if (imageData != null) {
                                            ImageCard(
                                                imageData = imageData,
                                                isBig = false,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }

                                    // Spacer if only 1 small item
                                    if (endIndex - (startIndex + 1) == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }

                            // Spacer between groups
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                },
                onError = {
                    item {
                        Text(text = "error")
                    }
                }
            )
        }
    }
}