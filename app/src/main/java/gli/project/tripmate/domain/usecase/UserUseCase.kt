package gli.project.tripmate.domain.usecase

import gli.project.tripmate.domain.model.user.User
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    suspend fun userRegister(email: String, password: String) : Flow<ResultResponse<Unit>>
    suspend fun userLogin(email: String, password: String) : Flow<ResultResponse<Unit>>
    suspend fun userLogout()
    suspend fun getCurrentUser() : User?
    suspend fun changePassword(oldPassword: String, newPassword: String) : Flow<ResultResponse<Unit>>
    suspend fun editUserProfile(user: User) : Flow<ResultResponse<Unit>>
}