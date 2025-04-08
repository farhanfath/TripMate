package gli.project.tripmate.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gli.project.tripmate.domain.usecase.UserUseCase
import gli.project.tripmate.domain.util.ResultResponse
import gli.project.tripmate.presentation.state.auth.AppEntryState
import gli.project.tripmate.presentation.state.auth.AuthState
import gli.project.tripmate.presentation.util.LogUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    private val _appEntryState = MutableStateFlow(AppEntryState())
    val appEntryState = _appEntryState.asStateFlow()

    init {
        getCurrentUser()
    }

    fun userRegister(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(
                onProcess = true,
                registerStatus = ResultResponse.Loading
            ) }
            _authState.update {
                it.copy(registerStatus = userUseCase.userRegister(email, password))
            }
        }
    }

    fun userLogin(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(
                onProcess = true,
                loginStatus = ResultResponse.Loading
            ) }
            _authState.update {
                it.copy(loginStatus = userUseCase.userLogin(email, password))
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _authState.update { it.copy(
                onProcess = true,
                currentUser = null
            ) }
            val user = userUseCase.getCurrentUser()
            _authState.update { it.copy(
                onProcess = false,
                currentUser = user
            ) }
            LogUtil.d("UserViewModel", "User ditemukan: $user")
        }
    }

    fun resetAllAuthStatus() {
        _authState.update {
            it.copy(
                onProcess = false,
                loginStatus = ResultResponse.Initiate,
                registerStatus = ResultResponse.Initiate,
                changePasswordStatus = ResultResponse.Initiate,
                editProfileStatus = ResultResponse.Initiate
            )
        }
    }

    fun userLogout() {
        viewModelScope.launch {
            userUseCase.userLogout()
            _authState.update { it.copy(
                currentUser = null
            ) }
        }
    }

    fun checkUserLoginStatus() {
        viewModelScope.launch {
            val user = userUseCase.getCurrentUser()
            _appEntryState.value = AppEntryState(
                isLoading = false,
                user = user
            )
        }
    }
}