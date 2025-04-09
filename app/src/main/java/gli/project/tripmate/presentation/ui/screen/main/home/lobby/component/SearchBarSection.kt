package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_12
import gli.project.tripmate.presentation.ui.theme.padding_16
import gli.project.tripmate.presentation.ui.theme.padding_20
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_50
import gli.project.tripmate.presentation.ui.theme.padding_8

@Composable
fun SearchBar(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = padding_50, start = padding_20, end = padding_20)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(padding_10)
    ) {
        // Search Bar
        Surface(
            color = Color.White,
            modifier = Modifier
                .graphicsLayer {
                    shape = RoundedCornerShape(32.dp)
                    clip = true
                    shadowElevation = 10f
                }
                .weight(6f)
                .clickable {
                    onSearchClick()
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = padding_16, vertical = padding_12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(padding_8))

                    Text(
                        text = "Where are you want to go...",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Light
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }

        // Filter button
        ElevatedButton(
            onClick = {
                onFilterClick()
            },
            shape = CircleShape,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = padding_4,
                pressedElevation = padding_8
            )
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter"
            )
        }
    }
}