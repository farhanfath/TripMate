package gli.project.tripmate.presentation.ui.screen.main.lobby

import android.app.Activity
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
import androidx.compose.runtime.collectAsState
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
import androidx.paging.compose.collectAsLazyPagingItems
import gli.project.tripmate.presentation.receiver.LocationProviderChangedReceiver
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.CategorySection
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.Greeting
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.HistoryView
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.Nearby
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.SearchBar
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.filter.FilterBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.location.LocationBottomSheet
import gli.project.tripmate.presentation.ui.screen.main.lobby.component.location.LocationPermissionCard
import gli.project.tripmate.presentation.viewmodel.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.RecentViewViewModel

@Composable
fun LobbyScreen(
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

    // place state handler
    val placesState by placesViewModel.placesState.collectAsState()
    val nearbyPlacesState = placesState.nearbyPlaces.collectAsLazyPagingItems()

    // recentView state handler
    val recentView by recentViewViewModel.recentView.collectAsState(emptyList())

    // location state handler
    val locationState by locationViewModel.locationState.collectAsStateWithLifecycle()

    // location gps request handler
    val locationRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        locationViewModel.handleLocationServiceResult(activityResult.resultCode == Activity.RESULT_OK)
    }

    val locationReceiver = remember {
        LocationProviderChangedReceiver().apply {
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

    // filter bottom sheet state handler
    var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }

    // get permission status every time permission result changes
    LaunchedEffect(permissionResult) {
        if (permissionResult) {
            locationViewModel.refreshPermissionStatus()
        }
        locationViewModel.updatePermissionStatusAndGetLocation()
    }

    // Register receiver for gps every launch
    LaunchedEffect(Unit) {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        context.registerReceiver(locationReceiver, filter)
    }

    // Unregister receiver for gps every dispose
    DisposableEffect(Unit) {
        onDispose {
            context.unregisterReceiver(locationReceiver)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .height(30.dp)
                    .fillMaxWidth()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            item {
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
                            onFilterClick = {
                                showFilterBottomSheet = true
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.size(20.dp))
                CategorySection(
                    onCategoryDetailClick = { name, endpoint ->
                        onCategoryDetailClick(name, endpoint)
                    }
                )
            }
            item {
                HistoryView(
                    recentViewData = recentView,
                    recentViewViewModel = recentViewViewModel,
                    onDetailClick = onDetailClick
                )
            }
            if (locationState.permissionGranted && locationState.isLocationEnabled) {
                item {
                    Nearby(
                        onDetailClick = onDetailClick,
                        onAddRecentView = { place ->
                            recentViewViewModel.addRecentView(place)
                        },
                        placeData = nearbyPlacesState,
                        onSeeMoreClick = onSeeMoreClick
                    )
                }
            } else {
                item {
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

        LocationBottomSheet(
            isPermissionIssue = locationState.isPermissionIssue,
            onDismiss = {
                locationViewModel.dismissLocationDialog()
            },
            onConfirm = {
                if (locationState.isPermissionIssue) {
                    onLocationRequestPermission()
                } else {
                    locationViewModel.enableLocationService(context) { intentSenderRequest ->
                        locationRequestLauncher.launch(intentSenderRequest)
                    }
                }
                locationViewModel.dismissLocationDialog()
            },
            isVisible = locationState.showLocationBottomSheet
        )

        FilterBottomSheet(
            onConfirm = { filterName, filterEndpoint ->
                onCategoryDetailClick(filterName, filterEndpoint)
            },
            onDismiss = { showFilterBottomSheet = false },
            isVisible = showFilterBottomSheet,
            placesViewModel = placesViewModel
        )
    }
}