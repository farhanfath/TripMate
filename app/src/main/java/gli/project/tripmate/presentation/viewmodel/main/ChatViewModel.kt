package gli.project.tripmate.presentation.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.usecase.ChatUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.state.main.ChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {
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
                ResultResponse.Initiate -> {}
            }
        }
    }
}

data class Message(val text: String, val isUser: Boolean)