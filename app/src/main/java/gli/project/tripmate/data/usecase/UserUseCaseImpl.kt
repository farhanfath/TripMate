package gli.project.tripmate.data.usecase

import gli.project.tripmate.domain.model.user.User
import gli.project.tripmate.domain.repository.UserRepository
import gli.project.tripmate.domain.usecase.UserUseCase
import gli.project.tripmate.domain.util.ResultResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UserUseCase {

    override suspend fun userRegister(
        email: String,
        password: String
    ): Flow<ResultResponse<Unit>> = userRepository.userRegister(email,password)

    override suspend fun userLogin(email: String, password: String): Flow<ResultResponse<Unit>> = userRepository.userLogin(email, password)

    override suspend fun userLogout() = userRepository.userLogout()

    override suspend fun getCurrentUser(): User? = userRepository.getCurrentUser()

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Flow<ResultResponse<Unit>> = userRepository.changePassword(oldPassword, newPassword)

    override suspend fun editUserProfile(user: User): Flow<ResultResponse<Unit>> = userRepository.editUserProfile(user)
}