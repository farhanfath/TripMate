package gli.project.tripmate.presentation.ui.screen.main.home.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import gli.project.tripmate.presentation.viewmodel.auth.UserViewModel

@Composable
fun ProfileScreen(
    onUserLogout: () -> Unit
) {
    val userViewModel : UserViewModel = hiltViewModel()
    val context = LocalContext.current
    Column {
        Text(
            text = "Profile Screen"
        )
        Button(
            onClick = {
                userViewModel.userLogout()
                Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show()
                onUserLogout()
            }
        ) {
            Text(
                text = "Logout"
            )
        }
    }
}