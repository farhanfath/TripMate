package gli.project.tripmate.presentation.util

import androidx.compose.runtime.Composable
import gli.project.tripmate.domain.util.ResultResponse

@Composable
fun <T> HandlerResponseCompose(
    response: ResultResponse<T>,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (data: T) -> Unit,
    onError: @Composable (error: ResultResponse.Error) -> Unit
) {
    when (response) {
        is ResultResponse.Loading -> onLoading()
        is ResultResponse.Success -> onSuccess(response.data)
        is ResultResponse.Error -> onError(response)
    }
}