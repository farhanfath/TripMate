package gli.project.tripmate.presentation.ui.state

import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.viewmodel.Message

data class ChatState(
    val message : List<Message> = emptyList(),
    val chat: ResultResponse<Unit> = ResultResponse.Success(Unit)
)