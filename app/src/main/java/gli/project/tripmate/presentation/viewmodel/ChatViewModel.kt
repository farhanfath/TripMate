package gli.project.tripmate.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.usecase.ChatUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.ui.state.ChatState
import gli.project.tripmate.presentation.util.LogUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {
//    private val _messages = MutableStateFlow<List<Message>>(emptyList())
//    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
//
//    private val _chatState = MutableStateFlow<ResultResponse<Unit>>(ResultResponse.Success(Unit))
//    val chatState: StateFlow<ResultResponse<Unit>> = _chatState.asStateFlow()

    private val _chatState = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            _chatState.update {
                it.copy(
                    message = it.message + Message(userMessage, isUser = true),
                    chat = ResultResponse.Loading
                )
            }

            when (val result = chatUseCase.getChatResponse(userMessage)) {
                is ResultResponse.Success -> {
                    _chatState.update {
                        it.copy(
                            message = it.message + Message(result.data, isUser = false),
                            chat = ResultResponse.Success(Unit)
                        )
                    }
                }
                is ResultResponse.Error -> {
                    _chatState.update {
                        it.copy(
                            message = it.message + Message("Error: ${result.message}", isUser = false),
                            chat = result
                        )
                    }
                }
                ResultResponse.Loading -> {}
            }
        }
    }

//    var isLoading by mutableStateOf(false)
//        private set
//
//    fun sendMessage(userMessage: String) {
//        viewModelScope.launch {
//            _chatState.update { it.copy(
//                message = it.message += Message(userMessage, isUser = true)
//            ) }
//            _messages.value += Message(userMessage, isUser = true)
//            _chatState.value = ResultResponse.Loading  // Indikator Loading
//
//            when (val result = chatUseCase.getChatResponse(userMessage)) {
//                is ResultResponse.Success -> {
//                    _messages.value += Message(result.data, isUser = false)
//                    _chatState.value = ResultResponse.Success(Unit)
//                }
//                is ResultResponse.Error -> {
//                    _messages.value += Message("Error: ${result.message}", isUser = false)
//                    _chatState.value = result
//                }
//                ResultResponse.Loading -> {}
//            }
//        }
//    }
}

data class Message(val text: String, val isUser: Boolean)