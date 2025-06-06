package gli.project.tripmate.presentation.ui.screen.main.detail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gli.project.tripmate.R
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.presentation.ui.theme.padding_20
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_40
import gli.project.tripmate.presentation.ui.theme.padding_8
import gli.project.tripmate.presentation.ui.theme.size_0_5
import gli.project.tripmate.presentation.ui.theme.size_10
import gli.project.tripmate.presentation.ui.theme.size_16
import gli.project.tripmate.presentation.util.extensions.emptyTextHandler
import gli.project.tripmate.presentation.util.extensions.formatOperatingHours
import gli.project.tripmate.presentation.util.extensions.ratingFormat

@Composable
fun DetailInformation(
    data: DetailPlace,
    placeName: String,
    totalReview: Int,
    ratingStats: Double
) {
    Row(
        modifier = Modifier
            .padding(top = padding_40, start = padding_20, end = padding_20, bottom = padding_20)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = emptyTextHandler(placeName, stringResource(id = R.string.name_not_available)),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.width(190.dp),
            )
            Spacer(modifier = Modifier.size(size_10))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_4)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(size_16)
                )
                Box(
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        text = data.address,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.basicMarquee()
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Box(
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        text = formatOperatingHours(data.openingHours),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.basicMarquee()
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(width = size_0_5, color = Color.LightGray)
        ) {
            Row(
                modifier = Modifier.padding(padding_8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    modifier = Modifier.size(size_16),
                    tint = Color.Yellow
                )
                Text(
                    text = ratingFormat(ratingStats),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = padding_4)
                )
                Text(
                    text = "($totalReview reviews)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}