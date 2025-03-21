package gli.project.tripmate.presentation.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gli.project.tripmate.presentation.receiver.LocationProviderChangedReceiver
import gli.project.tripmate.presentation.ui.component.location.LocationPermissionHandler
import gli.project.tripmate.presentation.ui.navigation.MainNavHost
import gli.project.tripmate.presentation.ui.theme.TripMateTheme
import gli.project.tripmate.presentation.viewmodel.ChatViewModel
import gli.project.tripmate.presentation.viewmodel.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var locationReceiver: LocationProviderChangedReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            // viewModel
            val placeViewModel : PlacesViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()
            val chatViewModel: ChatViewModel = hiltViewModel()

            MainApp {
                LocationPermissionHandler(locationViewModel) { permissionResult, onLocationRequestPermission ->
                    MainNavHost(
                        navController = navController,
                        placeViewModel = placeViewModel,
                        locationViewModel = locationViewModel,
                        chatViewModel = chatViewModel,
                        permissionResult = permissionResult,
                        onLocationRequestPermission = onLocationRequestPermission
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationReceiver?.let { unregisterReceiver(it) }
    }
}

@Composable
fun MainApp(content: @Composable () -> Unit) {
    TripMateTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        content()
    }
}