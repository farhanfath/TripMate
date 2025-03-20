package gli.project.tripmate.presentation.ui.screen.lobby.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.outlined.NotListedLocation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_20
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_6
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_130
import gli.project.tripmate.presentation.ui.theme.size_15
import gli.project.tripmate.presentation.ui.theme.size_180
import gli.project.tripmate.presentation.ui.theme.size_200
import gli.project.tripmate.presentation.ui.theme.size_30

@Composable
fun Nearby() {
    Column {
        Row(
            modifier = Modifier
                .padding(start = padding_20, end = padding_20, top = padding_20)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Discover Nearby",
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
            items(10) {
                NearbyItem()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NearbyPreview(modifier: Modifier = Modifier) {
    Nearby()
}

@Preview
@Composable
fun NearbyItem() {
    Card(
        modifier = Modifier
            .padding(vertical = padding_10, horizontal = padding_4)
            .height(size_200)
            .width(size_180),
        elevation = CardDefaults.cardElevation(padding_4),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(size_130)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .height(size_130)
                        .clip(RoundedCornerShape(padding_12))
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(padding_12)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        FilledIconButton(
                            onClick = {},
                            modifier = Modifier.size(size_30),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Icon(
                                modifier = Modifier.padding(padding_6),
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = padding_4, horizontal = padding_8),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(padding_4)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.NotListedLocation,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(size_15)
                                )
                                Text(
                                    text = "6 km",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Canary Hill"
                )
                Text(
                    text = "from $250",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Light
                    )
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
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