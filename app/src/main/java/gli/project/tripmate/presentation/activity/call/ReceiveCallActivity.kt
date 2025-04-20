package gli.project.tripmate.presentation.activity.call

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import gli.project.tripmate.data.helper.agora.AgoraVoiceCallManager
import gli.project.tripmate.data.helper.notification.CallNotificationManager
import gli.project.tripmate.presentation.activity.MainActivity
import gli.project.tripmate.presentation.ui.screen.call.IncomingCallScreen
import gli.project.tripmate.presentation.ui.theme.TripMateTheme

class ReceiveCallActivity : ComponentActivity() {
    private val agoraManager by lazy { AgoraVoiceCallManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CallNotificationManager(this).cancelNotification()

        // Get data from intent
        val appId = intent.getStringExtra("EXTRA_APP_ID") ?: ""
        val channelName = intent.getStringExtra("EXTRA_CHANNEL_NAME") ?: ""
        val token = intent.getStringExtra("EXTRA_TOKEN") ?: ""

        Log.d("CallActivity", "Started with: appId=$appId, channel=$channelName")

        setupCallbacks()
        // Start voice call
        if (appId.isNotEmpty() && channelName.isNotEmpty() && token.isNotEmpty()) {
            agoraManager.startVoiceCall(appId, channelName, token)
        } else {
            Log.e("CallActivity", "Missing call parameters")
            finish()
        }

        enableEdgeToEdge()
        setContent {
            TripMateTheme {
                IncomingCallScreen(
                    onAcceptCall = {
                        val intent = Intent(this, ActiveCallActivity::class.java)
                        intent.putExtra(ActiveCallActivity.INTENT_STATUS, "notification")
                        startActivity(intent)
                        finish()
                    },
                    onDeclineCall = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    callerName = "Customer"
                )
            }
        }
    }

    private fun setupCallbacks() {
        agoraManager.setOnJoinChannelSuccessCallback { channel ->
            Log.d("CallActivity", "Successfully joined channel: $channel")
        }

        agoraManager.setOnUserJoinedCallback { uid ->
            Log.d("CallActivity", "User joined: $uid")
        }

        agoraManager.setOnUserOfflineCallback { uid ->
            Log.d("CallActivity", "User offline: $uid")
        }

        agoraManager.setOnErrorCallback { code, message ->
            Log.e("CallActivity", "Error: $message (code: $code)")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraManager.cleanup()
    }
}