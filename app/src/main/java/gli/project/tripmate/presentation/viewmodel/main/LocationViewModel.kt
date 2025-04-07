package gli.project.tripmate.presentation.viewmodel.main

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.data.helper.LocationHelper
import gli.project.tripmate.domain.usecase.LocationUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.state.main.LocationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val locationDataStore: LocationDataStore,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _locationState = MutableStateFlow(LocationState())
    val locationState = _locationState.asStateFlow()

    init {
        checkPermissionStatus()
        updateLocationServiceStatus()
    }

    private fun checkPermissionStatus() {
        val isPermissionGranted = locationHelper.isLocationPermissionGranted()
        _locationState.update { it.copy(
            permissionGranted = isPermissionGranted
        )}

        if (isPermissionGranted) {
            if (isLocationServiceEnabled()) {
                getLocation()
            } else {
                _locationState.update {
                    it.copy(
                        isPermissionIssue = false,
                        showLocationBottomSheet = true
                    )
                }
            }
        }
    }

    fun updatePermissionStatusAndGetLocation() {
        val actuallyGranted = locationHelper.isLocationPermissionGranted()

        _locationState.update { it.copy(
            permissionGranted = actuallyGranted
        )}

        if (actuallyGranted) {
            if (isLocationServiceEnabled()) {
                getLocation()
            } else {
                _locationState.update {
                    it.copy(
                        isPermissionIssue = false,
                        showLocationBottomSheet = true
                    )
                }
            }
        } else {
            _locationState.update {
                it.copy(
                    isPermissionIssue = true,
                    showLocationBottomSheet = true
                )
            }
        }
    }

    fun refreshPermissionStatus() {
        checkPermissionStatus()
    }

    private fun getLocation() {
        viewModelScope.launch {
            locationUseCase.getCurrentLocation().collect { result ->
                _locationState.update {
                    it.copy(
                        currentLocation = result
                    )
                }
                if (result is ResultResponse.Success) {
                    locationDataStore.updateLocation(
                        result.data.latitude,
                        result.data.longitude
                    )
                }
            }
        }
    }

    private fun updateLocationServiceStatus() {
        viewModelScope.launch {
            val wasEnabled = _locationState.value.isLocationEnabled
            _locationState.update { it.copy(
                isLocationEnabled = locationUseCase.isLocationServiceEnabled()
            )}

            if (wasEnabled && !_locationState.value.isLocationEnabled && _locationState.value.permissionGranted) {
                _locationState.update { it.copy(
                    isPermissionIssue = false,
                    showLocationBottomSheet = true
                )}
            }
        }
    }

    fun enableLocationService(
        context: Context,
        makeRequest: (IntentSenderRequest) -> Unit
    ) {
        locationHelper.enableLocationRequest(context = context, makeRequest = makeRequest)
    }

    fun dismissLocationDialog() {
        _locationState.update {
            it.copy(
                showLocationBottomSheet = false
            )
        }
    }

    fun handleLocationServiceResult(resultOk: Boolean) {
        if (resultOk) {
            updateLocationServiceStatus()
        } else {
            if (!isLocationServiceEnabled()) {
                _locationState.update {
                    it.copy(
                        isPermissionIssue = false,
                        showLocationBottomSheet = true
                    )
                }
            }
        }
    }

    fun onLocationProviderEnabled() {
        updateLocationServiceStatus()
        if (_locationState.value.permissionGranted && _locationState.value.isLocationEnabled) {
            getLocation()
        }
    }

    fun onLocationProviderDisabled() {
        updateLocationServiceStatus()
        if (_locationState.value.permissionGranted) {
            _locationState.update {
                it.copy(
                    isPermissionIssue = false,
                    showLocationBottomSheet = true
                )
            }
        }
    }

    private fun isLocationServiceEnabled(): Boolean {
        return locationUseCase.isLocationServiceEnabled()
    }
}