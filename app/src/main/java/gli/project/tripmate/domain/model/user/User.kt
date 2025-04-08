package gli.project.tripmate.domain.model.user

data class User(
    val id: String = "",
    val email: String = "",
    val userName: String = "",
    val lastLogin: Long = 0
)