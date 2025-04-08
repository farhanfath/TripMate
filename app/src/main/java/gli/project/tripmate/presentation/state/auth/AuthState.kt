package gli.project.tripmate.presentation.state.auth

import gli.project.tripmate.domain.model.user.User
import gli.project.tripmate.domain.util.ResultResponse

data class AuthState (
    val onProcess: Boolean = false,
    val loginStatus: ResultResponse<Unit> = ResultResponse.Initiate,
    val registerStatus: ResultResponse<Unit> = ResultResponse.Initiate,
    val changePasswordStatus: ResultResponse<Unit> = ResultResponse.Initiate,
    val editProfileStatus: ResultResponse<Unit> = ResultResponse.Initiate,
    val currentUser: User? = null
)