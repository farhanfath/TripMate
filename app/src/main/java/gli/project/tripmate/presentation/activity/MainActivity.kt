package gli.project.tripmate.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import gli.project.tripmate.presentation.ui.screen.lobby.LobbyScreen
import gli.project.tripmate.presentation.ui.theme.TripMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp {
                LobbyScreen()
            }
        }
    }
}

@Composable
fun MainApp(content: @Composable () -> Unit) {
    TripMateTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        content()
    }
}