package gli.project.tripmate.presentation.ui.screen.main.lobby.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R

@Composable
fun HistoryView(
    onDetailClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
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
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(6) {
                HistoryViewItem(
                    onDetailClick = onDetailClick
                )
            }
        }
    }
}

@Composable
fun HistoryViewItem(
    onDetailClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .width(280.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 10f
            }
            .clickable {
                onDetailClick()
            }
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .height(75.dp),
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
                            modifier = Modifier.size(15.dp)
                        )
                        Box(
                            modifier = Modifier.width(80.dp)
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
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "4.5",
                            modifier = Modifier.padding(horizontal = 2.dp),
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

@Preview(showBackground = true)
@Composable
fun HistoryViewPreview() {
    HistoryView(
        onDetailClick = {}
    )
}
