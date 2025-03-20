package gli.project.tripmate.presentation.util

import android.util.Log
import gli.project.tripmate.BuildConfig

object LogUtil {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
}