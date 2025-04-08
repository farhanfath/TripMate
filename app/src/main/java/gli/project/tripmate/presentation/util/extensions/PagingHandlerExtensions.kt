package gli.project.tripmate.presentation.util.extensions

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

/**
 * Simple extension function to handle paging states
 */
fun <T : Any> handlePagingState(
    items: LazyPagingItems<T>,
    onLoading: () -> Unit,
    onError: (Throwable) -> Unit,
    onSuccess: () -> Unit
) {
    when (val state = items.loadState.refresh) {
        is LoadState.Loading -> onLoading()
        is LoadState.Error -> onError(state.error)
        is LoadState.NotLoading -> onSuccess()
    }
}

/**
 * Simple extension function to handle paging append states (pagination)
 */
fun <T : Any> handlePagingAppendState(
    items: LazyPagingItems<T>,
    onLoading: () -> Unit,
    onError: (Throwable) -> Unit,
    onNotLoading: () -> Unit = {}
) {
    when (val state = items.loadState.append) {
        is LoadState.Loading -> onLoading()
        is LoadState.Error -> onError(state.error)
        is LoadState.NotLoading -> onNotLoading()
    }
}

/**
 * composable extension function to handle paging states
 */
@Composable
fun <T : Any> HandleComposePagingState(
    items: LazyPagingItems<T>,
    onLoading: @Composable () -> Unit,
    onError: @Composable (Throwable) -> Unit,
    onSuccess: @Composable () -> Unit
) {
    when (val state = items.loadState.refresh) {
        is LoadState.Loading -> onLoading()
        is LoadState.Error -> onError(state.error)
        is LoadState.NotLoading -> onSuccess()
    }
}