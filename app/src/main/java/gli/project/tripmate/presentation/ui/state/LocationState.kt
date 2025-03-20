package gli.project.tripmate.presentation.ui.state

import gli.project.tripmate.domain.model.LocationModel
import gli.project.tripmate.domain.util.ResultResponse

data class LocationState (
    val currentLocation : ResultResponse<LocationModel> = ResultResponse.Loading,
    val permissionGranted : Boolean = false,
    val isLocationEnabled : Boolean = false,
    val showLocationBottomSheet : Boolean = false,
    val isPermissionIssue : Boolean = false
)