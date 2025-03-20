package gli.project.tripmate.presentation.ui.screen.main.lobby.component.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LocationPermissionCard(
    isPermissionGranted: Boolean,
    isLocationEnabled: Boolean,
    onRequestPermission: () -> Unit,
    onEnableLocation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Untuk melihat tempat terdekat",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Berikan izin lokasi untuk menemukan tempat menarik di sekitar Anda",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (!isPermissionGranted) {
                        onRequestPermission()
                    } else if (!isLocationEnabled) {
                        onEnableLocation()
                    }
                }
            ) {
                Text(
                    text = if (!isPermissionGranted) "Berikan Izin Lokasi" else "Aktifkan Layanan Lokasi"
                )
            }
        }
    }
}