package gli.project.tripmate.presentation.ui.screen.auth.splash

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@Composable
fun SplashScreen(
    onUserAvailable: () -> Unit,
    onUserNotAvailable: () -> Unit
) {
    val viewModel: UserViewModel = hiltViewModel()
    val state by viewModel.appEntryState.collectAsState()
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            if (state.user == null) {
                onUserNotAvailable()
            } else {
                onUserAvailable()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkUserLoginStatus()
    }

    Scaffold {
        Text(
            modifier = Modifier.padding(it),
            text = "splash"
        )
    }
}