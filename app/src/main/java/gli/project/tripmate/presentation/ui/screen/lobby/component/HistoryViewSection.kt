package gli.project.tripmate.presentation.ui.screen.lobby.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_2
import gli.project.tripmate.presentation.ui.theme.padding_20
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.size_15
import gli.project.tripmate.presentation.ui.theme.size_280
import gli.project.tripmate.presentation.ui.theme.size_75
import gli.project.tripmate.presentation.ui.theme.size_80

@Composable
fun HistoryView() {
    Column {
        Row(
            modifier = Modifier
                .padding(start = padding_20, end = padding_20, top = padding_20)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recently Viewed",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = padding_10)
        ) {
            items(6) {
                HistoryViewItem()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryViewPreview() {
    HistoryView()
}

@Composable
fun HistoryViewItem() {
    Card(
        modifier = Modifier
            .padding(vertical = padding_10, horizontal = padding_4)
            .width(size_280),
        elevation = CardDefaults.cardElevation(padding_4),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = padding_16, horizontal = padding_12)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding_10)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                modifier = Modifier
                    .size(size_80)
                    .clip(RoundedCornerShape(padding_10))
            )
            Column(
                modifier = Modifier
                    .height(size_75),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hotel Canary Inn & Restaurant",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = "Free Breakfast Available",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(size_15)
                        )
                        Box(
                            modifier = Modifier.width(size_80)
                        ) {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = "Jakarta, Indonesia",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                            tint = Color.Yellow,
                            modifier = Modifier.size(size_15)
                        )
                        Text(
                            text = "4.5",
                            modifier = Modifier.padding(horizontal = padding_2),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "(125)",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}