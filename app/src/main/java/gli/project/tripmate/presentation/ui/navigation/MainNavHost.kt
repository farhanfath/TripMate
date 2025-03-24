package gli.project.tripmate.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.screen.chat.ChatScreen
import gli.project.tripmate.presentation.ui.screen.detail.DetailScreen
import gli.project.tripmate.presentation.ui.screen.main.MainNavScreen
import gli.project.tripmate.presentation.viewmodel.ChatViewModel
import gli.project.tripmate.presentation.viewmodel.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.PlacesViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    placeViewModel: PlacesViewModel,
    chatViewModel: ChatViewModel,
    locationViewModel: LocationViewModel,
    permissionResult: Boolean,
    onLocationRequestPermission: () -> Unit
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
                    onDetailClick = { placeId, placeName ->
                        navController.navigate(
                            MainNavigation.DetailTour(
                                placeId = placeId,
                                placeName = placeName
                            )
                        )
                    },
                    placeViewModel = placeViewModel,
                    locationViewModel = locationViewModel,
                    permissionResult = permissionResult,
                    onLocationRequestPermission = onLocationRequestPermission,
                    onChatAIClick = {
                        navController.navigate(
                            MainNavigation.ChatAI
                        )
                    }
                )
            }
        }

        composable<MainNavigation.DetailTour> {entry ->
            val detailPlace = entry.toRoute<MainNavigation.DetailTour>()
            DetailScreen(
                placeId = detailPlace.placeId,
                placeName = detailPlace.placeName,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<MainNavigation.ChatAI> {
            ChatScreen(
                chatViewModel = chatViewModel,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}