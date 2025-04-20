package gli.project.tripmate

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import gli.project.tripmate.data.helper.notification.CallNotificationManager
import gli.project.tripmate.presentation.receiver.initializeSupabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class App : Application()