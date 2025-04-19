package gli.project.tripmate.presentation.ui.screen.main.chat.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun RouteLocationCardMap(
    latitude: Double,
    longitude: Double,
    locationName: String,
    locationDetail: String
) {
    val context = LocalContext.current
    val location = remember { LatLng(latitude, longitude) }

    // Set camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    // Map UI settings
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            compassEnabled = true
        )
    }

    // Map properties
    val mapProperties = remember {
        MapProperties(
            isMyLocationEnabled = false
        )
    }

    // Function to open Google Maps for viewing route
    val openGoogleMapsForDirections = {
        // Using "dir" instead of "navigation" to show directions without starting navigation
        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)

        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback just in case
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(browserIntent)
        }
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = mapUiSettings,
                    properties = mapProperties,
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = locationName,
                        snippet = "Lat: $latitude, Long: $longitude"
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = locationName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = locationDetail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = openGoogleMapsForDirections,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Petunjuk Arah")
            }
        }
    }

}