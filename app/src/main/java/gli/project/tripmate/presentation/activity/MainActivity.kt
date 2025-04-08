package gli.project.tripmate.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gli.project.tripmate.presentation.receiver.LocationProviderChangedReceiver
import gli.project.tripmate.presentation.ui.component.main.location.LocationPermissionHandler
import gli.project.tripmate.presentation.ui.navigation.main.MainNavHost
import gli.project.tripmate.presentation.ui.theme.TripMateTheme
import gli.project.tripmate.presentation.viewmodel.main.ChatViewModel
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

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
            val recentViewViewModel: RecentViewViewModel = hiltViewModel()

            MainApp {
                LocationPermissionHandler(locationViewModel) { permissionResult, onLocationRequestPermission ->
                    MainNavHost(
                        navController = navController,
                        placeViewModel = placeViewModel,
                        locationViewModel = locationViewModel,
                        chatViewModel = chatViewModel,
                        recentViewViewModel = recentViewViewModel,
                        permissionResult = permissionResult,
                        onLocationRequestPermission = onLocationRequestPermission,
                        onUserLogout = {
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
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