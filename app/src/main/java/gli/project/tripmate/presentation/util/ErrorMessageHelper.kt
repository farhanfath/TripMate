package gli.project.tripmate.presentation.util

import android.content.Context
import gli.project.tripmate.R
import gli.project.tripmate.data.util.PagingError
import gli.project.tripmate.domain.util.ErrorMessage
import gli.project.tripmate.domain.util.ResultResponse

object ErrorMessageHelper {
    fun getThrowableErrorMessage(error: Throwable, context: Context): String {
        return when (error) {
            is PagingError.NetworkError -> context.getString(R.string.no_connection_message)
            is PagingError.ServerError -> context.getString(R.string.failed_server_connect)
            else -> error.message ?: context.getString(R.string.unknown_error)
        }
    }

    fun getUiErrorMessage(error: ResultResponse.Error, context: Context): String {
        return when (error.message) {
            ErrorMessage.NETWORK_ERROR -> context.getString(R.string.no_connection_message)
            ErrorMessage.SERVER_ERROR -> context.getString(R.string.failed_server_connect)
            else -> error.message
        }
    }
}