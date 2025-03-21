package gli.project.tripmate.presentation.ui.screen.main.lobby.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun Feature() {
    Row(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
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
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .size(70.dp)
            .graphicsLayer {
                shape = RoundedCornerShape(10.dp)
                clip = true
                shadowElevation = 20f
            }
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
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = "Hotels"
            )
        }
    }
//    Card(
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.onPrimary
//        ),
//        modifier = Modifier
//            .padding(horizontal = 10.dp, vertical = 4.dp)
//            .size(70.dp)
//            .weight(1f)
//            .clickable {
//
//            }
//    ) {
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.Bed,
//                contentDescription = "",
//                modifier = Modifier.padding(bottom = 6.dp)
//            )
//            Text(
//                text = "Hotels"
//            )
//        }
//    }
}