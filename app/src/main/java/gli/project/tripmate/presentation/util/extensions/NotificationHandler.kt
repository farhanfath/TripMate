package gli.project.tripmate.presentation.util.extensions

import android.util.Log
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import gli.project.tripmate.presentation.receiver.initializeSupabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun DisposableEffectScope.getNotification(
    coroutineScope: CoroutineScope,
    onGetNotification: (String, String, String, String) -> Unit
) : DisposableEffectResult {
    val (channel, job, subscribeJob) = initializeSupabase(
        coroutineScope = coroutineScope
    ) { appIdData, channelNameData, tokenData, userId ->
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                onGetNotification(appIdData, channelNameData, tokenData, userId)
            }
        }
    }

    return onDispose {
        // Membatalkan job
        job.cancel()
        subscribeJob.cancel()

        // Membersihkan channel
        coroutineScope.launch {
            try {
                channel.unsubscribe()
                Log.d("Supabase", "Channel unsubscribed")
            } catch (e: Exception) {
                Log.e("Supabase", "Failed to unsubscribe channel: ${e.message}")
            }
        }
    }
}