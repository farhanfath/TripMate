package gli.project.tripmate.presentation.ui.navigation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.screen.call.N8nActiveCallScreen
import gli.project.tripmate.presentation.ui.screen.call.component.CustomerServiceDialog
import gli.project.tripmate.presentation.ui.screen.main.category.CategoryScreen
import gli.project.tripmate.presentation.ui.screen.main.chat.ConversationScreen
import gli.project.tripmate.presentation.ui.screen.main.detail.DetailScreen
import gli.project.tripmate.presentation.ui.screen.main.home.MainNavScreen
import gli.project.tripmate.presentation.ui.screen.main.home.favorite.FavoriteScreen
import gli.project.tripmate.presentation.ui.screen.main.home.rating_collection.RatingCollectionScreen
import gli.project.tripmate.presentation.ui.screen.main.more.MoreNearbyScreen
import gli.project.tripmate.presentation.ui.screen.main.search.SearchScreen
import gli.project.tripmate.presentation.ui.screen.product.ProductChatScreen
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    placeViewModel: PlacesViewModel,
    recentViewViewModel: RecentViewViewModel,
    locationViewModel: LocationViewModel,
    permissionResult: Boolean,
    onLocationRequestPermission: () -> Unit,
    onUserLogout: () -> Unit,
    onCustomerServiceIntent: () -> Unit
) {
    var showCustomerServiceDialog by remember { mutableStateOf(false) }

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
                        navigateToDetail(navController, placeId, placeName)
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
                    },
                    onFavoriteClick = {
                        navController.navigate(
                            MainNavigation.Favorite
                        )
                    },
                    onHistoryRatingClick = {
                        navController.navigate(
                            MainNavigation.RatingCollection
                        )
                    },
                    onCustomerServiceCallClick = {
                        navController.navigate(
                            MainNavigation.Call
                        )
                    },
                    onProductRecommendationClick = {
                        navController.navigate(
                            MainNavigation.ProductChat
                        )
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
            ConversationScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onFeatureActionRequest = { actionRequest ->
                    when (actionRequest) {
                        "rating" -> {
                            navController.navigate(
                                MainNavigation.RatingCollection
                            )
                        }
                        "collection" -> {
                            navController.navigate(
                                MainNavigation.Favorite
                            )
                        }
                        "customer_service" -> {
                            showCustomerServiceDialog = true
                        }
                        else -> {}
                    }
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
                    navigateToDetail(navController, placeId, placeName)
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
                    navigateToDetail(navController, placeId, placeName)
                }
            )
        }

        composable<MainNavigation.Search> {
            SearchScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onDetailClick = { placeId, placeName ->
                    navigateToDetail(navController, placeId, placeName)
                },
                placesViewModel = placeViewModel
            )
        }


        composable<MainNavigation.Favorite> {
            FavoriteScreen(
                onDetailClick = { placeId, placeName ->
                    navigateToDetail(navController, placeId, placeName)
                },
                onBackClick = {
                    navController.navigateUp()
                },
                onExploreClick = {
                    navController.navigate(
                        MainNavigation.Lobby
                    ) {
                        popUpTo<MainNavigation.Main>()
                    }
                }
            )
        }

        composable<MainNavigation.RatingCollection> {
            RatingCollectionScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<MainNavigation.Call> {
            N8nActiveCallScreen(
                onEndCallClick = {
                    navController.navigateUp()
                },
                assistantName = "Ai Agent",
                callStartTime = System.currentTimeMillis(),
                onCustomerServiceIntent = {
                    onCustomerServiceIntent()
                }
            )
        }

        composable<MainNavigation.ProductChat> {
            ProductChatScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }

    AnimatedVisibility(
        visible = showCustomerServiceDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CustomerServiceDialog(
            onDismissRequest = { showCustomerServiceDialog = false },
            onConnectConfirmed = {
                onCustomerServiceIntent()
            }
        )
    }
}

private fun navigateToDetail(
    navController: NavHostController,
    placeId: String,
    placeName: String
) {
    navController.navigate(
        MainNavigation.DetailTour(
            placeId = placeId,
            placeName = placeName
        )
    )
}