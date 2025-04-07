package gli.project.tripmate.presentation.ui.component.main.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel

@Composable
fun LocationPermissionHandler(
    locationViewModel: LocationViewModel,
    content: @Composable (
        permissionResult: Boolean,
        onLocationRequestPermission: () -> Unit
    ) -> Unit
) {
    // location permission status
    var permissionResult = checkLocationPermission(LocalContext.current)
    var shouldShowRationale by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        val hasPermission = checkLocationPermission(context)
        permissionResult = hasPermission

        // Update shouldShowRationale after permission request
        shouldShowRationale = shouldShowLocationPermissionRationale(context)

        locationViewModel.updatePermissionStatusAndGetLocation()
    }

    // permission from settings
    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val hasPermission = checkLocationPermission(context)
        permissionResult = hasPermission
        shouldShowRationale = shouldShowLocationPermissionRationale(context)
        locationViewModel.updatePermissionStatusAndGetLocation()
    }

    // request permission from dialog
    val onLocationRequestPermission = {
        if (!permissionResult) {
            if (shouldShowRationale) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                openAppSettings(context, settingsLauncher)
            }
        }
    }

    content(permissionResult, onLocationRequestPermission)
}

// Helper functions
fun shouldShowLocationPermissionRationale(context: Context): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        context as Activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun openAppSettings(
    context: Context,
    settingsLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    settingsLauncher.launch(intent)
}