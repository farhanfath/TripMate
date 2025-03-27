package gli.project.tripmate.domain.util

sealed class ResultResponse<out T> {
    data object Loading : ResultResponse<Nothing>()
    data class Success<T>(val data: T) : ResultResponse<T>()
    data class Error(val message: String) : ResultResponse<Nothing>()
}