package gli.project.tripmate.data.helper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataStore @Inject constructor() {
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    fun updateLocation(latitude: Double, longitude: Double) {
        _currentLocation.value = Pair(latitude, longitude)
    }
}