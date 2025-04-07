package gli.project.tripmate.domain.repository

import gli.project.tripmate.domain.model.user.User
import gli.project.tripmate.domain.util.ResultResponse

interface UserRepository {
    suspend fun userRegister(email: String, password: String) : ResultResponse<Unit>
    suspend fun userLogin(email: String, password: String) : ResultResponse<Unit>
    suspend fun userLogout()
    suspend fun getCurrentUser() : User?
    suspend fun changePassword(oldPassword: String, newPassword: String) : ResultResponse<Unit>
    suspend fun editUserProfile(user: User) : ResultResponse<Unit>
}