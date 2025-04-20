package gli.project.tripmate.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gli.project.tripmate.data.helper.notification.CallNotificationManager
import gli.project.tripmate.presentation.activity.call.ActiveCallActivity
import gli.project.tripmate.presentation.receiver.LocationProviderChangedReceiver
import gli.project.tripmate.presentation.ui.component.main.location.LocationPermissionHandler
import gli.project.tripmate.presentation.ui.navigation.main.MainNavHost
import gli.project.tripmate.presentation.ui.theme.TripMateTheme
import gli.project.tripmate.presentation.util.extensions.getNotification
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
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current

            // notification handler
            DisposableEffect(Unit) {
                getNotification(
                    coroutineScope = coroutineScope,
                    onGetNotification = { appId, channelName, token, userId ->
                        if(userId == "\"customer\"") {
                            val notificationManager = CallNotificationManager(context)
                            notificationManager.showIncomingCallNotification(
                                appId = appId,
                                channelName = channelName,
                                token = token,
                                title = "Customer Service Call",
                                message = "Tap to answer the call"
                            )
                        }
                    }
                )
            }

            val navController = rememberNavController()

            // viewModel
            val placeViewModel : PlacesViewModel = hiltViewModel()
            val locationViewModel: LocationViewModel = hiltViewModel()
            val recentViewViewModel: RecentViewViewModel = hiltViewModel()

            MainApp {
                LocationPermissionHandler(locationViewModel) { permissionResult, onLocationRequestPermission ->
                    MainNavHost(
                        navController = navController,
                        placeViewModel = placeViewModel,
                        locationViewModel = locationViewModel,
                        recentViewViewModel = recentViewViewModel,
                        permissionResult = permissionResult,
                        onLocationRequestPermission = onLocationRequestPermission,
                        onUserLogout = {
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onCustomerServiceIntent = {
                            val intent = Intent(this, ActiveCallActivity::class.java)
                            intent.putExtra(ActiveCallActivity.INTENT_STATUS, "ai")
                            startActivity(intent)
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