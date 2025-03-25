package gli.project.tripmate.data.util

sealed class PagingError(message: String) : Exception(message) {
    data object NetworkError : PagingError("Network Error") {
        private fun readResolve(): Any = NetworkError
    }

    data object ServerError : PagingError("Server Error") {
        private fun readResolve(): Any = ServerError
    }

    data object UnknownError : PagingError("Unknown Error") {
        private fun readResolve(): Any = UnknownError
    }
}