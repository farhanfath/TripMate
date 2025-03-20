package gli.project.tripmate.presentation.ui.screen.lobby.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gli.project.tripmate.presentation.ui.theme.padding_10
import gli.project.tripmate.presentation.ui.theme.padding_4
import gli.project.tripmate.presentation.ui.theme.padding_5
import gli.project.tripmate.presentation.ui.theme.padding_6
import gli.project.tripmate.presentation.ui.theme.size_70

@Composable
fun Feature() {
    Row(
        modifier = Modifier
            .padding(vertical = padding_5, horizontal = padding_10)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(4) {
            FeatureItem()
        }
    }
}

@Composable
fun RowScope.FeatureItem() {
    Card(
        elevation = CardDefaults.cardElevation(padding_4),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .padding(horizontal = padding_10, vertical = padding_4)
            .size(size_70)
            .weight(1f)
            .clickable {

            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Outlined.Bed,
                contentDescription = "",
                modifier = Modifier.padding(bottom = padding_6)
            )
            Text(
                text = "Hotels"
            )
        }
    }
}