package gli.project.tripmate.presentation.ui.screen.main.category.component

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_130
import gli.project.tripmate.presentation.ui.theme.size_15
import gli.project.tripmate.presentation.util.extensions.emptyTextHandler

@Composable
fun NearbyPlaceLongItem(
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
                    .height(size_130)
            ) {
                CustomImageLoader(
                    url = place.image,
                    modifier = Modifier
                        .height(size_130)
                        .clip(RoundedCornerShape(padding_12))
                        .fillMaxWidth(),
                    scale = ContentScale.Crop
                )
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
                        text = emptyTextHandler(place.name, stringResource(id = R.string.name_not_available))
                    )
                }
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
            }
        }
    }
}

@Preview
@Composable
fun NearbyPlaceByCategoryItemPreview() {
    NearbyPlaceLongItem(
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