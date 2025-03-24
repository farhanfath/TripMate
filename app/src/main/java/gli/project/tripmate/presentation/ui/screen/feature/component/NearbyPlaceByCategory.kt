package gli.project.tripmate.presentation.ui.screen.feature.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.presentation.ui.component.CustomImageLoader

@Composable
fun NearbyPlaceByCategoryItem(
    onDetailClick: () -> Unit,
    place: Place,
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .height(200.dp)
            .fillMaxWidth()
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 10f
            }
            .clickable {
                onDetailClick()
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(130.dp)
            ) {
                CustomImageLoader(
                    url = place.image,
                    modifier = Modifier
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth(),
                    scale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        FilledIconButton(
                            onClick = {},
                            modifier = Modifier.size(30.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Icon(
                                modifier = Modifier.padding(6.dp),
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
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.NotListedLocation,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "10 km",
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
                Box(
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = place.name
                    )
                }
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
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = place.city,
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

@Preview
@Composable
fun NearbyPlaceByCategoryItemPreview() {
    NearbyPlaceByCategoryItem(
        onDetailClick = {},
        place = Place(
            placeId = "",
            name = "Jakarta Raya",
            country = "Indonesia",
            city = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ornare faucibus sollicitudin. Nulla id mattis erat, nec imperdiet odio. Morbi fermentum ac massa id finibus. Cras faucibus viverra metus ut pretium. Quisque nibh enim, vehicula id semper a, maximus at sem. In dui magna, sodales quis turpis sed, pellentesque porttitor sapien. Ut id scelerisque ipsum, et consectetur magna. Maecenas aliquet, elit id vulputate feugiat, leo dolor cursus turpis, vel venenatis leo nibh sed sapien. Nulla orci lectus, aliquam non nisi in, consectetur blandit nisi. Nullam augue sapien, vulputate ac elementum sit amet, imperdiet non nisl.",
            image = "",
            lat = 0.0,
            lon = 0.0
        )
    )
}