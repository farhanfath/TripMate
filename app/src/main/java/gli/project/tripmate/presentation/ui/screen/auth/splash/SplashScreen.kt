package gli.project.tripmate.presentation.ui.screen.auth.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.util.iconUrl
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomImageLoader(
            url = iconUrl,
            desc = "icon Logo",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge.copy(

            )
        )
    }
}