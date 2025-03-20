package gli.project.tripmate.data.repository

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import gli.project.tripmate.data.helper.LocationHelper
import gli.project.tripmate.domain.model.LocationModel
import gli.project.tripmate.domain.repository.LocationRepository
import gli.project.tripmate.domain.util.ErrorMessage
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationHelper: LocationHelper
) : LocationRepository {
    override suspend fun getCurrentLocation(): Flow<ResultResponse<LocationModel>> = flow {
        emit(ResultResponse.Loading)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            emit(ResultResponse.Error(ErrorMessage.LOCATION_PERMISSION_ERROR))
            return@flow
        }

        if (!isLocationServiceEnabled()) {
            emit(ResultResponse.Error(ErrorMessage.LOCATION_SERVICE_ERROR))
            return@flow
        }

        try {
            val cancellationToken = CancellationTokenSource()

            val location = suspendCancellableCoroutine<Location?> { continuation ->
                continuation.invokeOnCancellation {
                    cancellationToken.cancel()
                }
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationToken.token
                ).addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }

            if (location != null) {
                emit(ResultResponse.Success(
                    LocationModel(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                ))
            } else {
                emit(ResultResponse.Error(ErrorMessage.LOCATION_NOT_FOUND))
            }
        } catch (e: Exception) {
            ResultResponse.Error("Error getting location: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    override fun isLocationServiceEnabled(): Boolean {
        return locationHelper.isConnected()
    }
}