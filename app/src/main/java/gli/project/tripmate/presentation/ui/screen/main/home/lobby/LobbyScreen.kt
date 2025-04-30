package gli.project.tripmate.presentation.ui.screen.main.home.lobby

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.domain.model.Place
import gli.project.tripmate.presentation.receiver.LocationProviderChangedReceiver
import gli.project.tripmate.presentation.state.main.LocationState
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.CategorySection
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.Greeting
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.HistoryView
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.Nearby
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.SearchBar
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.filter.FilterBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.location.LocationBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.component.location.LocationPermissionCard
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun LobbyScreen(
    onSearchClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    placesViewModel: PlacesViewModel,
    locationViewModel: LocationViewModel,
    recentViewViewModel: RecentViewViewModel,
    permissionResult: Boolean,
    onLocationRequestPermission: () -> Unit,
    onCategoryDetailClick: (name: String, endpoint: String) -> Unit,
    onSeeMoreClick: () -> Unit
) {
    val context = LocalContext.current

    // Gunakan collectAsStateWithLifecycle untuk semua state
    val placesState by placesViewModel.placesState.collectAsStateWithLifecycle()
    val recentView by recentViewViewModel.recentView.collectAsStateWithLifecycle(initialValue = emptyList())
    val locationState by locationViewModel.locationState.collectAsStateWithLifecycle()

    // Optimalkan untuk mencegah rekomposisi yang tidak perlu
    val nearbyPlacesState = remember(placesState.nearbyPlaces) {
        placesState.nearbyPlaces
    }.collectAsLazyPagingItems()

    // location gps request handler, dipertahankan dengan remember
    val locationRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        locationViewModel.handleLocationServiceResult(activityResult.resultCode == Activity.RESULT_OK)
    }

    // filter bottom sheet state handler
    var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

    // Location receiver dioptimalkan
    val locationReceiver = remember {
        object : BroadcastReceiver() {
            private var locationListener: LocationProviderChangedReceiver.LocationListener? = null

            fun init(listener: LocationProviderChangedReceiver.LocationListener) {
                this.locationListener = listener
            }

            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false

                    if (isGpsEnabled) {
                        locationListener?.onEnabled()
                    } else {
                        locationListener?.onDisabled()
                    }
                }
            }
        }.apply {
            init(object : LocationProviderChangedReceiver.LocationListener {
                override fun onEnabled() {
                    locationViewModel.onLocationProviderEnabled()
                }

                override fun onDisabled() {
                    locationViewModel.onLocationProviderDisabled()
                }
            })
        }
    }

    // Optimalkan LaunchedEffect dengan mempertimbangkan dependency key
    LaunchedEffect(permissionResult) {
        if (permissionResult) {
            locationViewModel.refreshPermissionStatus()
            locationViewModel.updatePermissionStatusAndGetLocation()
        }
    }

    // Register receiver for gps pada LaunchedEffect dengan key yang tepat
    LaunchedEffect(Unit) {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(locationReceiver, filter)
    }

    // DisposableEffect dipertahankan
    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(locationReceiver)
        }
    }

    Scaffold(
        topBar = {
            TopAppBarSection()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            // Gunakan keys untuk item-item penting
            item(key = "header") {
                HeaderSection(onSearchClick, onFilterClick = { showFilterBottomSheet = true })
            }

            item(key = "category") {
                Spacer(modifier = Modifier.size(20.dp))
                CategorySection(onCategoryDetailClick = onCategoryDetailClick)
            }

            item(key = "history") {
                HistoryView(
                    recentViewData = recentView,
                    recentViewViewModel = recentViewViewModel,
                    onDetailClick = onDetailClick
                )
            }

            // Optimalkan dengan mempertimbangkan dependencies yang jelas
            if (locationState.permissionGranted && locationState.isLocationEnabled) {
                item(key = "nearby") {
                    NearbySection(
                        nearbyPlacesState = nearbyPlacesState,
                        onDetailClick = onDetailClick,
                        onAddRecentView = { place -> recentViewViewModel.addRecentView(place) },
                        onSeeMoreClick = onSeeMoreClick
                    )
                }
            } else {
                item(key = "location_permission") {
                    LocationPermissionCard(
                        isPermissionGranted = locationState.permissionGranted,
                        isLocationEnabled = locationState.isLocationEnabled,
                        onRequestPermission = onLocationRequestPermission,
                        onEnableLocation = {
                            locationViewModel.enableLocationService(context) { intentSenderRequest ->
                                locationRequestLauncher.launch(intentSenderRequest)
                            }
                        }
                    )
                }
            }
        }

        // Dialog dan BottomSheet dilepaskan ke komposabel terpisah
        LocationBottomSheetSection(
            locationState = locationState,
            onDismiss = { locationViewModel.dismissLocationDialog() },
            onConfirm = {
                if (locationState.isPermissionIssue) {
                    onLocationRequestPermission()
                } else {
                    locationViewModel.enableLocationService(context) { intentSenderRequest ->
                        locationRequestLauncher.launch(intentSenderRequest)
                    }
                }
                locationViewModel.dismissLocationDialog()
            }
        )

        FilterBottomSheetSection(
            isVisible = showFilterBottomSheet,
            placesViewModel = placesViewModel,
            onConfirm = onCategoryDetailClick,
            onDismiss = { showFilterBottomSheet = false }
        )
    }
}

@Composable
private fun TopAppBarSection() {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .height(30.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun HeaderSection(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .height(200.dp)
                .fillMaxWidth(),
        )
        Column {
            Greeting()
            SearchBar(
                onSearchClick = onSearchClick,
                onFilterClick = onFilterClick
            )
        }
    }
}

@Composable
private fun NearbySection(
    nearbyPlacesState: LazyPagingItems<Place>,
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onAddRecentView: (Place) -> Unit,
    onSeeMoreClick: () -> Unit
) {
    Nearby(
        onDetailClick = onDetailClick,
        onAddRecentView = onAddRecentView,
        placeData = nearbyPlacesState,
        onSeeMoreClick = onSeeMoreClick
    )
}

@Composable
private fun LocationBottomSheetSection(
    locationState: LocationState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    LocationBottomSheet(
        isPermissionIssue = locationState.isPermissionIssue,
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        isVisible = locationState.showLocationBottomSheet
    )
}

@Composable
private fun FilterBottomSheetSection(
    isVisible: Boolean,
    placesViewModel: PlacesViewModel,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    FilterBottomSheet(
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        isVisible = isVisible,
        placesViewModel = placesViewModel
    )
}