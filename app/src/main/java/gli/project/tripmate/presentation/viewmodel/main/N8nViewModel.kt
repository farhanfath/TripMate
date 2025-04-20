package gli.project.tripmate.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.data.helper.LocationDataStore
import gli.project.tripmate.data.helper.n8n.SpeechRecognizerManager
import gli.project.tripmate.data.helper.n8n.TextToSpeechManager
import gli.project.tripmate.data.remote.n8n.model.WebhookResponse
import gli.project.tripmate.domain.model.n8n.ConversationItem
import gli.project.tripmate.domain.model.n8n.type.InputType
import gli.project.tripmate.domain.model.n8n.type.N8nType
import gli.project.tripmate.domain.usecase.N8nUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class N8nViewModel @Inject constructor(
    private val n8nUseCase: N8nUseCase,
    val speechRecognizerManager: SpeechRecognizerManager,
    val textToSpeechManager: TextToSpeechManager
) : ViewModel() {

    private val _conversation = MutableStateFlow<List<ConversationItem>>(emptyList())
    val conversation: StateFlow<List<ConversationItem>> = _conversation.asStateFlow()

    val isListening: StateFlow<Boolean> = speechRecognizerManager.isListening
    val isSpeaking: StateFlow<Boolean> = textToSpeechManager.isSpeaking
    val speechResult: StateFlow<String?> = speechRecognizerManager.speechResult

    // Untuk menampilkan hasil speech recognition secara real-time tanpa mengirimkan
    private val _currentTranscription = MutableStateFlow<String?>(null)
    val currentTranscription: StateFlow<String?> = _currentTranscription.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            // Pantau hasil speech recognition untuk tampilan real-time
            speechRecognizerManager.speechResult.collect { result ->
                _currentTranscription.value = result
            }
        }

        viewModelScope.launch {
            // Pantau status listening untuk memproses input saat berhenti mendengarkan
            speechRecognizerManager.isListening.collect { isActive ->
                if (!isActive && _currentTranscription.value?.isNotBlank() == true) {
                    // Proses input hanya ketika berhenti mendengarkan dan ada teks
                    val finalInput = _currentTranscription.value
                    _currentTranscription.value = null

                    finalInput?.let { processUserInput(it) }
                }
            }
        }
    }

    fun startListening() = speechRecognizerManager.startListening()
    fun stopListening() = speechRecognizerManager.stopListening()
    fun stopSpeaking() = textToSpeechManager.stop()

    // for conversation screen
    fun textProcessInput(input: String) {
        if (input.isNotBlank()) {
            viewModelScope.launch {
                addUserMessage(input, inputType = InputType.TEXT)

                _isLoading.value = true
                try {
                    val result = n8nUseCase.sendMessageToWebhook(input, inputType = InputType.TEXT)
                    result.onSuccess { response ->
                        addAssistantMessage(response)
                    }.onFailure { throwable ->
                        _error.value = "Error: ${throwable.message}"
                        addAssistantMessage(
                            WebhookResponse("Sorry, I'm having trouble connecting to the server.")
                        )
                    }
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    // for call screen
    private fun processUserInput(input: String) {
        if (input.isNotBlank()) {
            viewModelScope.launch {
                addUserMessage(input, inputType = InputType.VOICE)

                _isLoading.value = true
                try {
                    val result = n8nUseCase.sendMessageToWebhook(input, inputType = InputType.VOICE)
                    result.onSuccess { response ->
                        addAssistantMessage(response)
                        textToSpeechManager.speak(response.response)
                    }.onFailure { throwable ->
                        _error.value = "Error: ${throwable.message}"
                        addAssistantMessage(
                            WebhookResponse("Sorry, I'm having trouble connecting to the server.")
                        )
                    }
                } finally {
                    _isLoading.value = false
                    // Tidak perlu reset speech result lagi di sini karena sudah direset di flow
                }
            }
        }
    }

    // Fungsi untuk secara manual memproses transkripsi saat ini (opsional)
    fun submitCurrentTranscription() {
        _currentTranscription.value?.let {
            if (it.isNotBlank()) {
                processUserInput(it)
                _currentTranscription.value = null
            }
        }
    }

    private fun addUserMessage(message: String, inputType: InputType) {
        val userMessage = ConversationItem(
            text = message,
            isUser = true,
            inputType = inputType
        )
        _conversation.value += userMessage
    }

    private fun addAssistantMessage(response: WebhookResponse) {
        val assistantMessage = when (response.responseType) {
            N8nType.TRAVEL_LIST -> {
                ConversationItem(
                    text = response.response,
                    isUser = false,
                    travelSpots = response.travelSpots,
                    type = N8nType.TRAVEL_LIST
                )
            }
            N8nType.ROUTE -> {
                ConversationItem(
                    text = response.response,
                    isUser = false,
                    locationMap = response.locationMap,
                    type = N8nType.ROUTE
                )
            }
            else -> {
                ConversationItem(
                    text = response.response,
                    isUser = false
                )
            }
        }
        _conversation.value += assistantMessage
    }

    fun clearError() {
        _error.value = null
    }

    fun resetAudioServices() {
        // Reset speech recognizer
        (speechRecognizerManager as? SpeechRecognizerManager)?.initialize()

        // Reset text to speech
        (textToSpeechManager as? TextToSpeechManager)?.initialize()

        // Reset state
        _currentTranscription.value = null
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizerManager.destroy()
        textToSpeechManager.destroy()
    }
}

/**
 * ini
 */
//@HiltViewModel
//class N8nViewModel @Inject constructor(
//    private val n8nUseCase: N8nUseCase,
//    val speechRecognizerManager: SpeechRecognizerManager,
//    private val textToSpeechManager: TextToSpeechManager,
//    private val locationDataStore: LocationDataStore
//) : ViewModel() {
//
//    private val _conversation = MutableStateFlow<List<ConversationItem>>(emptyList())
//    val conversation: StateFlow<List<ConversationItem>> = _conversation.asStateFlow()
//
//    val isListening: StateFlow<Boolean> = speechRecognizerManager.isListening
//    val isSpeaking: StateFlow<Boolean> = textToSpeechManager.isSpeaking
//    val speechResult: StateFlow<String?> = speechRecognizerManager.speechResult
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            speechRecognizerManager.speechResult.collect { result ->
//                result?.let { processUserInput(it) }
//            }
//        }
//    }
//
//    fun startListening() = speechRecognizerManager.startListening()
//    fun stopListening() = speechRecognizerManager.stopListening()
//    fun stopSpeaking() = textToSpeechManager.stop()
//
//    fun processUserInput(input: String) {
//        if (input.isNotBlank()) {
//            viewModelScope.launch {
//                addUserMessage(input)
//
//                _isLoading.value = true
//                try {
//                    val result = n8nUseCase.sendMessageToWebhook(input)
//                    result.onSuccess { response ->
//                        addAssistantMessage(response)
//                        textToSpeechManager.speak(response.response)
//                    }.onFailure { throwable ->
//                        _error.value = "Error: ${throwable.message}"
//                        addAssistantMessage(
//                            WebhookResponse("Sorry, I'm having trouble connecting to the server.")
//                        )
//                    }
//                } finally {
//                    _isLoading.value = false
//                    speechRecognizerManager.resetSpeechResult()
//                }
//            }
//        }
//    }
//
//    private fun addUserMessage(message: String) {
//        val userMessage = ConversationItem(
//            text = message,
//            isUser = true
//        )
//        _conversation.value += userMessage
//    }
//
//    private fun addAssistantMessage(response: WebhookResponse) {
//        val assistantMessage = when (response.responseType) {
//            N8nType.TRAVEL_LIST -> {
//                ConversationItem(
//                    text = response.response,
//                    isUser = false,
//                    travelSpots = response.travelSpots,
//                    type = N8nType.TRAVEL_LIST
//                )
//            }
//            N8nType.ROUTE -> {
//                ConversationItem(
//                    text = response.response,
//                    isUser = false,
//                    locationMap = response.locationMap,
//                    type = N8nType.ROUTE
//                )
//            }
//            else -> {
//                ConversationItem(
//                    text = response.response,
//                    isUser = false
//                )
//            }
//        }
//        _conversation.value += assistantMessage
//    }
//
//    fun clearError() {
//        _error.value = null
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        speechRecognizerManager.destroy()
//        textToSpeechManager.destroy()
//    }
//}