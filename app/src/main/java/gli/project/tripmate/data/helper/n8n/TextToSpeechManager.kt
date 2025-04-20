package gli.project.tripmate.data.helper.n8n

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import gli.project.tripmate.presentation.util.extensions.parseMarkdown
import gli.project.tripmate.presentation.viewmodel.main.N8nViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextToSpeechManager @Inject constructor(
    private val application: Application
) {
    private var textToSpeech: TextToSpeech? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        initializeTextToSpeech()
    }

    // Tambahkan fungsi initialize publik untuk reinisialisasi
    fun initialize() {
        // Hentikan dan bersihkan instance lama
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null

        // Reset state
        _isSpeaking.value = false
        _isInitialized.value = false
        _error.value = null

        // Inisialisasi ulang
        initializeTextToSpeech()
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("id", "ID")) // Indonesia
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    _error.value = "Indonesian language is not supported, using default"
                    textToSpeech?.setLanguage(Locale.getDefault())
                }
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                        _error.value = "Error in speech synthesis"
                    }
                })
                _isInitialized.value = true
            } else {
                _error.value = "Failed to initialize Text-to-Speech"
            }
        }
    }

    fun speak(text: String) {
        if (_isInitialized.value) {
            textToSpeech?.speak(parseMarkdown(text), TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        } else {
            _error.value = "Text-to-Speech is not initialized yet"
            // Coba inisialisasi ulang jika belum diinisialisasi
            initialize()
            // Tambahkan penundaan singkat lalu coba bicara lagi
            Handler(Looper.getMainLooper()).postDelayed({
                if (_isInitialized.value) {
                    textToSpeech?.speak(parseMarkdown(text), TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
                }
            }, 500)
        }
    }

    fun stop() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }

    fun destroy() {
        stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        _isInitialized.value = false
    }
}

// Tambahkan extension function untuk N8nViewModel
fun N8nViewModel.initializeTextToSpeech() {
    (textToSpeechManager as? TextToSpeechManager)?.initialize()
}