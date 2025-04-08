package gli.project.tripmate.presentation.ui.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.screen.main.category.CategoryScreen
import gli.project.tripmate.presentation.ui.screen.main.chat.ChatScreen
import gli.project.tripmate.presentation.ui.screen.main.detail.DetailScreen
import gli.project.tripmate.presentation.ui.screen.main.home.MainNavScreen
import gli.project.tripmate.presentation.ui.screen.main.more.MoreNearbyScreen
import gli.project.tripmate.presentation.ui.screen.main.search.SearchScreen
import gli.project.tripmate.presentation.viewmodel.main.ChatViewModel
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    placeViewModel: PlacesViewModel,
    chatViewModel: ChatViewModel,
    recentViewViewModel: RecentViewViewModel,
    locationViewModel: LocationViewModel,
    permissionResult: Boolean,
    onLocationRequestPermission: () -> Unit,
    onUserLogout: () -> Unit
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
                    onSearchClick = {
                        navController.navigate(
                            MainNavigation.Search
                        )
                    },
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
                    recentViewViewModel = recentViewViewModel,
                    permissionResult = permissionResult,
                    onLocationRequestPermission = onLocationRequestPermission,
                    onChatAIClick = {
                        navController.navigate(
                            MainNavigation.ChatAI
                        )
                    },
                    onCategoryDetailClick = { categoryName, categoryEndpoint ->
                        navController.navigate(
                            MainNavigation.DetailCategory(
                                categoryName = categoryName,
                                categoryEndpoint = categoryEndpoint
                            )
                        )
                    },
                    onSeeMoreClick = {
                        navController.navigate(
                            MainNavigation.MoreNearby
                        )
                    },
                    onUserLogout = {
                        onUserLogout()
                    }
                )
            }
        }

        composable<MainNavigation.DetailTour> { entry ->
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

        composable<MainNavigation.DetailCategory> { entry ->
            val categoryFeature = entry.toRoute<MainNavigation.DetailCategory>()
            CategoryScreen(
                placeViewModel = placeViewModel,
                recentViewViewModel = recentViewViewModel,
                nameCategory = categoryFeature.categoryName,
                categoryType = categoryFeature.categoryEndpoint,
                onBackClick = {
                    navController.navigateUp()
                },
                onDetailClick = { placeId, placeName ->
                    navController.navigate(
                        MainNavigation.DetailTour(
                            placeId = placeId,
                            placeName = placeName
                        )
                    )
                }
            )
        }

        composable<MainNavigation.MoreNearby> {
            MoreNearbyScreen(
                placesViewModel = placeViewModel,
                onBackClick = {
                    navController.navigateUp()
                },
                onDetailClick = { placeId, placeName ->
                    navController.navigate(
                        MainNavigation.DetailTour(
                            placeId = placeId,
                            placeName = placeName
                        )
                    )
                }
            )
        }

        composable<MainNavigation.Search> {
            SearchScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onDetailClick = { placeId, placeName ->
                    navController.navigate(
                        MainNavigation.DetailTour(
                            placeId = placeId,
                            placeName = placeName
                        )
                    )
                },
                placesViewModel = placeViewModel
            )
        }
    }
}