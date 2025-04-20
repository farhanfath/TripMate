package gli.project.tripmate.data.helper.agora

import android.content.Context
import android.util.Log
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

// AgoraVoiceCallManager.kt
class AgoraVoiceCallManager(private val context: Context) {
    private var mRtcEngine: RtcEngine? = null
    private var onUserJoinedCallback: ((Int) -> Unit)? = null
    private var onUserOfflineCallback: ((Int) -> Unit)? = null
    private var onJoinChannelSuccessCallback: ((String?) -> Unit)? = null
    private var onErrorCallback: ((Int, String) -> Unit)? = null

    companion object {
        private const val TAG = "AgoraVoiceCallManager"
    }

    // Event handler untuk Agora RTC
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            Log.d(TAG, "onJoinChannelSuccess: channel=$channel, uid=$uid, elapsed=$elapsed")
            onJoinChannelSuccessCallback?.invoke(channel)
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined: uid=$uid, elapsed=$elapsed")
            onUserJoinedCallback?.invoke(uid)
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            Log.d(TAG, "onUserOffline: uid=$uid, reason=$reason")
            onUserOfflineCallback?.invoke(uid)
        }

        override fun onError(err: Int) {
            val errorMsg = when (err) {
                Constants.ERR_INVALID_APP_ID -> "Invalid App ID"
                Constants.ERR_INVALID_CHANNEL_NAME -> "Invalid Channel Name"
                Constants.ERR_INVALID_TOKEN -> "Invalid Token"
                Constants.ERR_TOKEN_EXPIRED -> "Token Expired"
                else -> "Error code: $err"
            }
            Log.e(TAG, "onError: $errorMsg")
            onErrorCallback?.invoke(err, errorMsg)
        }

        override fun onConnectionStateChanged(state: Int, reason: Int) {
            Log.d(TAG, "onConnectionStateChanged: state=$state, reason=$reason")
        }
    }

    // Setter untuk callbacks
    fun setOnUserJoinedCallback(callback: (Int) -> Unit) {
        onUserJoinedCallback = callback
    }

    fun setOnUserOfflineCallback(callback: (Int) -> Unit) {
        onUserOfflineCallback = callback
    }

    fun setOnJoinChannelSuccessCallback(callback: (String?) -> Unit) {
        onJoinChannelSuccessCallback = callback
    }

    fun setOnErrorCallback(callback: (Int, String) -> Unit) {
        onErrorCallback = callback
    }

    // Function utama untuk memulai panggilan suara
    fun startVoiceCall(appId: String, channelName: String, token: String) {
        try {
            Log.d(TAG, "Starting voice call with: appId=$appId, channel=$channelName, token length=${token.length}")

            // Pastikan cleanup sebelum inisialisasi baru
            cleanup()

            // Inisialisasi Agora SDK
            initializeAgoraVoiceSDK(appId)

            // Konfigurasi audio
            configureAudioSettings()

            // Join channel
            joinChannel(channelName, token)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start voice call: ${e.message}", e)
            onErrorCallback?.invoke(-1, "Failed to start voice call: ${e.message}")
        }
    }

    // Inisialisasi Agora SDK
    private fun initializeAgoraVoiceSDK(appId: String) {
        try {
            val config = RtcEngineConfig().apply {
                mContext = context
                mAppId = appId
                mEventHandler = mRtcEventHandler
            }
            Log.d(TAG, "Initializing RTC Engine with app ID: $appId")
            mRtcEngine = RtcEngine.create(config)
            Log.d(TAG, "RTC Engine created successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing RTC engine: ${e.message}", e)
            throw RuntimeException("Error initializing RTC engine: ${e.message}")
        }
    }

    // Konfigurasi audio sebelum join channel
    private fun configureAudioSettings() {
        mRtcEngine?.let { engine ->
            // Enable audio
            engine.enableAudio()

            // Set audio profile dan scenario
            engine.setAudioProfile(
                Constants.AUDIO_PROFILE_DEFAULT,
                Constants.AUDIO_SCENARIO_DEFAULT
            )

            // Enable echo cancellation
            engine.setParameters("{\"che.audio.enable.ns\":true}")
            engine.setParameters("{\"che.audio.enable.aec\":true}")

            Log.d(TAG, "Audio settings configured")
        }
    }

    // Bergabung ke channel
    private fun joinChannel(channelName: String, token: String) {
        try {
            val options = ChannelMediaOptions().apply {
                clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
                channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
                publishMicrophoneTrack = true
                autoSubscribeAudio = true
            }

            Log.d(TAG, "Joining channel: $channelName with token length: ${token.length} token $token")
            val result = mRtcEngine?.joinChannel(token, channelName, 0, options)
            Log.d(TAG, "joinChannel result: $result")

            if (result != 0) {
                Log.e(TAG, "Failed to join channel with error code: $result")
                onErrorCallback?.invoke(result ?: -1, "Failed to join channel with error code: $result")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error joining channel: ${e.message}", e)
            onErrorCallback?.invoke(-1, "Error joining channel: ${e.message}")
        }
    }

    // Membersihkan resources saat selesai
    fun cleanup() {
        try {
            Log.d(TAG, "Cleaning up Agora resources")
            mRtcEngine?.apply {
                stopPreview()
                leaveChannel()
                RtcEngine.destroy()
            }
            mRtcEngine = null
            Log.d(TAG, "Cleanup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error during cleanup: ${e.message}", e)
        }
    }

    // Fungsi untuk mute/unmute audio
    fun toggleMute(mute: Boolean) {
        try {
            Log.d(TAG, "Toggle mute: $mute")
            mRtcEngine?.muteLocalAudioStream(mute)
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling mute: ${e.message}", e)
        }
    }

    // Fungsi untuk mengaktifkan/menonaktifkan speaker
    fun enableSpeakerphone(enabled: Boolean) {
        try {
            Log.d(TAG, "Enable speakerphone: $enabled")
            mRtcEngine?.setEnableSpeakerphone(enabled)
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling speakerphone: ${e.message}", e)
        }
    }
}