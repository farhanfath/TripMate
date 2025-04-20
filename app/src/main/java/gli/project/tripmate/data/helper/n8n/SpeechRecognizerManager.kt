package gli.project.tripmate.data.helper.n8n

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import gli.project.tripmate.presentation.viewmodel.main.N8nViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRecognizerManager @Inject constructor(
    private val application: Application
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var volumeListener: ((Float) -> Unit)? = null

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _speechResult = MutableStateFlow<String?>(null)
    val speechResult: StateFlow<String?> = _speechResult.asStateFlow()

    private val _currentVolume = MutableStateFlow(0f)
    val currentVolume: StateFlow<Float> = _currentVolume.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // For silence detection
    private var lastSignificantVolumeTime = 0L
    private val silenceThresholdMs = 1500L // 1.5 seconds
    private val volumeThreshold = 3f // RMS threshold for silence

    // For continuous listening
    private var continuousListeningJob: Job? = null

    init {
        if (SpeechRecognizer.isRecognitionAvailable(application)) {
            initializeSpeechRecognizer()
        } else {
            _error.value = "Speech recognition is not available on this device"
        }
    }

    fun initialize() {
        speechRecognizer?.destroy()
        speechRecognizer = null

        _isListening.value = false
        _speechResult.value = null
        _error.value = null

        if (SpeechRecognizer.isRecognitionAvailable(application)) {
            initializeSpeechRecognizer()
        } else {
            _error.value = "Speech recognition is not available on this device"
        }
    }

    private fun initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _isListening.value = true
                _error.value = null
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {
                _currentVolume.value = rmsdB
                volumeListener?.invoke(rmsdB)

                // Update silence detection
                if (rmsdB > volumeThreshold) {
                    lastSignificantVolumeTime = System.currentTimeMillis()
                }
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _isListening.value = false
            }

            override fun onError(error: Int) {
                _isListening.value = false

                // Don't report "no match" as an error, it's normal when silence is detected
                if (error != SpeechRecognizer.ERROR_NO_MATCH) {
                    _error.value = "Error code: $error"
                }

                // Auto-restart if in continuous mode
                continuousListeningJob?.let { restartListening() }
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    _speechResult.value = matches[0]
                }
                _isListening.value = false

                // Auto-restart if in continuous mode
                continuousListeningJob?.let { restartListening() }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    _speechResult.value = matches[0]
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startListening() {
        _speechResult.value = null
        _error.value = null

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID") // Bahasa Indonesia
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "id-ID")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            speechRecognizer?.startListening(intent)
            lastSignificantVolumeTime = System.currentTimeMillis()
        } catch (e: Exception) {
            _error.value = "Error starting speech recognition: ${e.message}"
        }
    }

    fun startContinuousListening(coroutineScope: CoroutineScope) {
        continuousListeningJob = coroutineScope.launch {
            startListening()

            // Monitor for silence
            while (isActive) {
                delay(100) // Check every 100ms

                // If we're listening and silence is detected for longer than threshold
                if (_isListening.value) {
                    val now = System.currentTimeMillis()
                    if (now - lastSignificantVolumeTime > silenceThresholdMs) {
                        // Silence detected, stop listening
                        stopListening()

                        // Wait before restarting
                        delay(500)
                        if (isActive) startListening()
                    }
                }
            }
        }
    }

    @DelicateCoroutinesApi
    private fun restartListening() {
        // Small delay before restarting
        continuousListeningJob?.let { job ->
            job.cancel()
            continuousListeningJob = job.run {
                CoroutineScope(coroutineContext).launch {
                    delay(1000)
                    startListening()
                }
            }
        }
    }

    fun stopContinuousListening() {
        continuousListeningJob?.cancel()
        continuousListeningJob = null
        stopListening()
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    fun resetSpeechResult() {
        _speechResult.value = null
    }

    fun setVolumeListener(listener: (Float) -> Unit) {
        volumeListener = listener
    }

    fun destroy() {
        continuousListeningJob?.cancel()
        speechRecognizer?.destroy()
    }
}

fun N8nViewModel.startContinuousListening(coroutineScope: CoroutineScope) {
    (speechRecognizerManager as? SpeechRecognizerManager)?.startContinuousListening(coroutineScope)
}

fun N8nViewModel.stopContinuousListening() {
    (speechRecognizerManager as? SpeechRecognizerManager)?.stopContinuousListening()
}

fun N8nViewModel.initializeSpeechRecognizer() {
    (speechRecognizerManager as? SpeechRecognizerManager)?.initialize()
}

/**
 * ini
 */
//@Singleton
//class SpeechRecognizerManager @Inject constructor(
//    private val application: Application
//) {
//    private var speechRecognizer: SpeechRecognizer? = null
//
//    private val _isListening = MutableStateFlow(false)
//    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
//
//    private val _speechResult = MutableStateFlow<String?>(null)
//    val speechResult: StateFlow<String?> = _speechResult.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//
//    init {
//        if (SpeechRecognizer.isRecognitionAvailable(application)) {
//            initializeSpeechRecognizer()
//        } else {
//            _error.value = "Speech recognition is not available on this device"
//        }
//    }
//
//    private fun initializeSpeechRecognizer() {
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)
//        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
//            override fun onReadyForSpeech(params: Bundle?) {
//                _isListening.value = true
//                _error.value = null
//            }
//
//            override fun onBeginningOfSpeech() {}
//
//            override fun onRmsChanged(rmsdB: Float) {}
//
//            override fun onBufferReceived(buffer: ByteArray?) {}
//
//            override fun onEndOfSpeech() {
//                _isListening.value = false
//            }
//
//            override fun onError(error: Int) {
//                _isListening.value = false
//                _error.value = "Error code: $error"
//            }
//
//            override fun onResults(results: Bundle?) {
//                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                if (!matches.isNullOrEmpty()) {
//                    _speechResult.value = matches[0]
//                }
//                _isListening.value = false
//            }
//
//            override fun onPartialResults(partialResults: Bundle?) {}
//
//            override fun onEvent(eventType: Int, params: Bundle?) {}
//        })
//    }
//
//    fun startListening() {
//        _speechResult.value = null
//        _error.value = null
//
//        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID") // Bahasa Indonesia
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "id-ID")
//            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "id-ID")
//            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
//        }
//
//        try {
//            speechRecognizer?.startListening(intent)
//        } catch (e: Exception) {
//            _error.value = "Error starting speech recognition: ${e.message}"
//        }
//    }
//
//    fun stopListening() {
//        speechRecognizer?.stopListening()
//        _isListening.value = false
//    }
//
//    fun resetSpeechResult() {
//        _speechResult.value = null
//    }
//
//    fun destroy() {
//        speechRecognizer?.destroy()
//    }
//}