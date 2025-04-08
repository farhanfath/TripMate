package gli.project.tripmate.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import gli.project.tripmate.presentation.util.LogUtil

class LocationProviderChangedReceiver : BroadcastReceiver() {

    interface LocationListener {
        fun onEnabled()
        fun onDisabled()
    }

    private var locationListener: LocationListener? = null

    fun init(locationListener: LocationListener) {
        this.locationListener = locationListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            LogUtil.d("LocationProviderChangedReceiver", "onReceive: $isGpsEnabled")

            if (isGpsEnabled) {
                locationListener?.onEnabled()
            } else {
                locationListener?.onDisabled()
            }
        }
    }
}