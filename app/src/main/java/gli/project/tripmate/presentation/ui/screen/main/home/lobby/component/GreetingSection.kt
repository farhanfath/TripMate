package gli.project.tripmate.presentation.ui.screen.main.home.lobby.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.presentation.ui.component.common.CustomImageLoader
import gli.project.tripmate.presentation.util.touristIcon
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@Composable
fun Greeting() {
    val userViewModel : UserViewModel = hiltViewModel()
    val userState by userViewModel.authState.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 45.dp, bottom = 15.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Hi, ${userState.currentUser?.userName}",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = "Are you ready to explore the world",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        CustomImageLoader(
            url = touristIcon,
            modifier = Modifier
                .size(50.dp)
                .border(width = 0.5.dp, color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                .clip(CircleShape)
        )
    }
}