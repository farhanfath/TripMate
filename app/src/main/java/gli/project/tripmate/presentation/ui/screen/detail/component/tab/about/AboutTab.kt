package gli.project.tripmate.presentation.ui.screen.detail.component.tab.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gli.project.tripmate.domain.model.DetailPlace
import gli.project.tripmate.presentation.ui.screen.detail.component.LocationMapCard
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun AboutTab(
    detailData: DetailPlace,
    userRange: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        /**
         * Place Description
         */
        Text(
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Justify
            ),
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ac sem ex. Donec fermentum feugiat tellus. Proin hendrerit ullamcorper augue, ac laoreet quam convallis a. Donec volutpat nec turpis a feugiat. Vestibulum urna tortor, maximus sed lacus vitae, hendrerit congue leo. Etiam sed sem nec ligula sodales tempus vitae et justo. Nulla rutrum porta nibh sit amet consectetur. Nulla pulvinar dolor sed nunc accumsan bibendum. Aliquam eget molestie tellus. Quisque non justo non diam consequat vehicula."
        )

        Text(
            text = "Location on map",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        /**
         * Google Map Location by lat and lon
         */
        LocationMapCard(
            latitude = detailData.lat,
            longitude = detailData.lon,
            locationName = detailData.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            userRange = userRange
        )

        // Add more content to test scrolling
        repeat(5) { index ->
            Text(
                text = "Section ${index + 1}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )
            
            Text(
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Justify
                ),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ac sem ex. Donec fermentum feugiat tellus. Proin hendrerit ullamcorper augue, ac laoreet quam convallis a. Donec volutpat nec turpis a feugiat. Vestibulum urna tortor, maximus sed lacus vitae, hendrerit congue leo. Etiam sed sem nec ligula sodales tempus vitae et justo. Nulla rutrum porta nibh sit amet consectetur."
            )
        }
    }
}