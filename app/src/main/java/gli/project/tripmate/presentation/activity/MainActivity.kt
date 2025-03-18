package gli.project.tripmate.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import gli.project.tripmate.presentation.ui.navigation.MainNavHost
import gli.project.tripmate.presentation.ui.theme.TripMateTheme
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel : PlacesViewModel = hiltViewModel()

            MainApp {
                MainNavHost(
                    navController = navController,
                    viewModel = viewModel
                )
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