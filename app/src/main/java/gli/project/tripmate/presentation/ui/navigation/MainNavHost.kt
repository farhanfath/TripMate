package gli.project.tripmate.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.screen.detail.DetailScreen
import gli.project.tripmate.presentation.ui.screen.main.MainNavScreen

@Composable
fun MainNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainNavigation.Main
    ) {
        navigation<MainNavigation.Main>(
            startDestination = MainNavigation.Lobby
        ) {
            composable<MainNavigation.Lobby> {
                MainNavScreen(
                    onDetailClick = {
                        navController.navigate(MainNavigation.DetailTour)
                    }
                )
            }
        }

        composable<MainNavigation.DetailTour> {
            DetailScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<MainNavigation.ChatAI> {

        }
    }
}