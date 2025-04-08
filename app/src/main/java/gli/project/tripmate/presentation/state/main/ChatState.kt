package gli.project.tripmate.presentation.state.main

import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.viewmodel.main.Message

data class ChatState(
    val message : List<Message> = emptyList(),
    val chat: ResultResponse<Unit> = ResultResponse.Success(Unit)
)