package gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import gli.project.tripmate.domain.model.PexelImage
import gli.project.tripmate.presentation.ui.component.CustomImageLoader
import gli.project.tripmate.presentation.util.extensions.handlePagingState

@Composable
fun GalleryRowGrid(
    onShowMore: () -> Unit = {},
    imageList: LazyPagingItems<PexelImage>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        handlePagingState(
            items = imageList,
            onLoading = {

            },
            onSuccess = {
                // Only display first 21 images
                val visibleItemCount = minOf(11, imageList.itemCount)

                // Process items in groups of three (1 big + 2 small)
                val groupCount = (visibleItemCount + 2) / 3

                for (groupIndex in 0 until groupCount) {
                    item(key = "group_$groupIndex") {
                        val startIndex = groupIndex * 3
                        val endIndex = minOf(startIndex + 3, visibleItemCount)

                        if (startIndex < visibleItemCount) {
                            val imageData = imageList[startIndex]
                            if (imageData != null) {
                                ImageCard(
                                    imageData = imageData,
                                    isBig = true
                                )
                            }
                        }

                        // 2 items small in one row (if available)
                        if (startIndex + 1 < visibleItemCount) {
                            Column {
                                for (i in startIndex + 1 until endIndex) {
                                    val imageData = imageList[i]
                                    if (imageData != null) {
                                        ImageCard(
                                            imageData = imageData,
                                            isBig = false
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

                // Show more button as the last item if there are more than 21 images
                if (imageList.itemCount > 21) {
                    item(key = "show_more") {
                        ShowMoreCard(onShowMore = onShowMore)
                    }
                }
            },
            onError = {

            }
        )
    }
}

@Composable
fun ImageCard(imageData: PexelImage, isBig: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .width(230.dp)
            .height(if (isBig) 310.dp else 150.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        CustomImageLoader(
            url = imageData.originalSize,
            modifier = Modifier.fillMaxSize(),
            scale = ContentScale.Crop
        )
    }
}


@Composable
fun ShowMoreCard(onShowMore: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
            .height(310.dp)
            .clickable {
                onShowMore()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowCircleRight,
                    contentDescription = "show more",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "show more",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}