package gli.project.tripmate.presentation.activity.call

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import gli.project.tripmate.presentation.activity.MainActivity
import gli.project.tripmate.presentation.activity.MainApp
import gli.project.tripmate.presentation.ui.screen.call.ActiveCallScreen
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class ActiveCallActivity : ComponentActivity() {
    private val myAppId = "ad51e95058144d25b0b6822ed2e6e193"
    private val channelName = "CrossPlatformCall"
    private val token = "007eJxTYNCZpaucJc6dbGP9tcWZ6Z7niuCEAyL2ZbGLGd0Ki1r3T1ZgSEwxNUy1NDUwtTA0MUkxMk0ySDKzMDJKTTFKNUs1tDR+f4MzoyGQkYG3JpyRkQECQXxBBuei/OLigJzEkrT8olznxJwcBgYAnvwggQ=="

    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread {
                showToast("Joined channel $channel")
            }
        }
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                showToast("A user joined")
            }
        }
        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                showToast("User offline: $uid")
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check if all required permissions are granted
        val allGranted = permissions.entries.all { it.value }

        if (allGranted) {
            startVoiceCalling()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val intentStatus = intent.getStringExtra(INTENT_STATUS)

            MainApp {
                ActiveCallScreen(
                    onEndCallClick = {
                        if (intentStatus == "notification") {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            finish()
                        }
                    },
                    callerName = if (intentStatus == "notification") {
                        "Customer"
                    } else {
                        "Customer Service"
                    }
                )
            }
        }
        if (checkPermissions()) {
            startVoiceCalling()
        } else {
            requestPermissions()
        }
    }
    private fun checkPermissions(): Boolean {
        for (permission in getRequiredPermissions()) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    private fun requestPermissions() {
        permissionLauncher.launch(getRequiredPermissions())
    }
    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startVoiceCalling() {
        initializeAgoraVoiceSDK()
        joinChannel()
    }
    private fun initializeAgoraVoiceSDK() {
        try {
            val config = RtcEngineConfig().apply {
                mContext = baseContext
                mAppId = myAppId
                mEventHandler = mRtcEventHandler
            }
            mRtcEngine = RtcEngine.create(config)
        } catch (e: Exception) {
            throw RuntimeException("Error initializing RTC engine: ${e.message}")
        }
    }
    private fun joinChannel() {
        val options = ChannelMediaOptions().apply {
            clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            publishMicrophoneTrack = true
        }
        mRtcEngine?.joinChannel(token, channelName, 0, options)
    }
    override fun onDestroy() {
        super.onDestroy()
        cleanupAgoraEngine()
    }
    private fun cleanupAgoraEngine() {
        mRtcEngine?.apply {
            stopPreview()
            leaveChannel()
        }
        mRtcEngine = null
    }
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@ActiveCallActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val INTENT_STATUS = "STATUS"
    }
}