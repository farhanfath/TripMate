package gli.project.tripmate.presentation.state.auth

import gli.project.tripmate.domain.model.user.User

data class AppEntryState(
    val isLoading: Boolean = true,
    val user: User? = null
)