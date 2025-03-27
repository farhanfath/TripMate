package gli.project.tripmate.presentation.ui.screen.detail.component.tab.gallery.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_1
import gli.project.tripmate.presentation.ui.theme.size_120
import gli.project.tripmate.presentation.ui.theme.size_150
import gli.project.tripmate.presentation.ui.theme.size_230
import gli.project.tripmate.presentation.ui.theme.size_310
import gli.project.tripmate.presentation.ui.theme.size_32
import gli.project.tripmate.presentation.ui.theme.size_8

/**
 * TODO: delete soon
 */
data class StoreItem(
    val id: Int,
    val name: String,
    val imageUrl: String
)

/**
 * TODO: delete soon
 */
fun generateStoreItems(count: Int = 30): List<StoreItem> {

    return List(count) { index ->
        val width = 300 + (index % 3) * 100
        val height = 200 + (index % 4) * 50

        StoreItem(
            id = index,
            name = "example",
            imageUrl = "https://picsum.photos/$width/$height?random=$index"
        )
    }
}

@Composable
fun GalleryRowGrid(
    onShowMore: () -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding_8)
    ) {
        val allItems = generateStoreItems()
        val displayItems = allItems.take(21)

        // Process items in groups of three (1 big + 2 small)
        displayItems.chunked(3).forEachIndexed { _, group ->
            item {
                // 1 item big (full width)
                if (group.isNotEmpty()) {
                    ImageCard(
                        item = group[0],
                        isBig = true
                    )
                }

                // 2 items small in one row (if available)
                if (group.size > 1) {
                    Column {
                        for (i in 1 until minOf(3, group.size)) {
                            ImageCard(
                                item = group[i],
                                isBig = false,
                            )
                        }

                        // Spacer if only 1 small item
                        if (group.size == 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                // Spacer between groups
                Spacer(modifier = Modifier.height(padding_8))
            }
        }

        // Show more button as the last item in the row
        if (allItems.size > 21) {
            item {
                ShowMoreCard(onShowMore = onShowMore)
            }
        }
    }
}

@Composable
fun ImageCard(item: StoreItem, isBig: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(padding_4)
            .width(size_230)
            .height(if (isBig) size_310 else size_150),
        shape = RoundedCornerShape(padding_8),
        elevation = CardDefaults.cardElevation(defaultElevation = padding_4)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Placeholder warna sebagai background
            val backgroundColor = Color(
                red = (item.id * 50) % 256 / 255f,
                green = (item.id * 20) % 256 / 255f,
                blue = (item.id * 70) % 256 / 255f,
                alpha = 1f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        }
    }
}

@Composable
fun ShowMoreCard(onShowMore: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .padding(padding_4)
            .width(size_120)
            .height(size_310)
            .clickable {
                onShowMore()
            },
        elevation = CardDefaults.cardElevation(padding_4),
        border = BorderStroke(
            width = size_1,
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
                    modifier = Modifier.size(size_32)
                )
                Spacer(modifier = Modifier.height(size_8))
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

@Preview(showBackground = true)
@Composable
fun GalleryRowGridPreview() {
    GalleryRowGrid()
}