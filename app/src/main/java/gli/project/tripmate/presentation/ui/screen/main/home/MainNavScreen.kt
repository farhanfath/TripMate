package gli.project.tripmate.presentation.ui.screen.main.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.component.common.CustomTopBar
import gli.project.tripmate.presentation.ui.component.main.BottomNavShape
import gli.project.tripmate.presentation.ui.navigation.main.bottomnav.BottomNavRoute
import gli.project.tripmate.presentation.ui.navigation.main.bottomnav.navItems
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.screen.main.home.lobby.LobbyScreen
import gli.project.tripmate.presentation.ui.screen.main.home.profile.ProfileScreen
import gli.project.tripmate.presentation.viewmodel.main.LocationViewModel
import gli.project.tripmate.presentation.viewmodel.main.PlacesViewModel
import gli.project.tripmate.presentation.viewmodel.main.RecentViewViewModel

@Composable
fun MainNavScreen(
    navController: NavHostController = rememberNavController(),
    mainActions: MainActions,
    placeViewModel: PlacesViewModel,
    locationViewModel: LocationViewModel,
    recentViewViewModel: RecentViewViewModel,
    permissionResult: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            // Menggunakan key untuk mencegah rekomposisi yang tidak perlu
            key(currentDestination?.route) {
                CustomTopBar()
            }
        },
        floatingActionButton = {
            ChatFAB(onClick = mainActions.onChatAIClick)
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            OptimizedNavigationBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        MainNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            mainActions = mainActions,
            placeViewModel = placeViewModel,
            locationViewModel = locationViewModel,
            recentViewViewModel = recentViewViewModel,
            permissionResult = permissionResult
        )
    }
}

@Composable
private fun ChatFAB(onClick: () -> Unit) {
    // Ekstrak FAB ke composable terpisah untuk reuse dan optimasi
    FloatingActionButton(
        modifier = Modifier
            .size(70.dp)
            .offset(y = 50.dp),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(4.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Chat AI",
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun OptimizedNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar(
        modifier = Modifier.clip(
            BottomNavShape(
                cornerRadius = with(LocalDensity.current) { 10.dp.toPx() },
                dockRadius = with(LocalDensity.current) { 45.dp.toPx() },
            )
        ),
    ) {
        navItems.forEach { navItem ->
            val selectedBarItem = currentDestination?.hierarchy?.any {
                it.hasRoute(navItem.route::class)
            } == true

            // Menggunakan remember untuk menghindari rekomposisi yang tidak perlu
            val indicatorWidth by animateDpAsState(
                targetValue = if (selectedBarItem) 24.dp else 0.dp,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f)
            )

            NavigationBarItem(
                selected = selectedBarItem,
                icon = {
                    NavigationItemContent(
                        navItem = navItem,
                        isSelected = selectedBarItem,
                        indicatorWidth = indicatorWidth
                    )
                },
                label = {
                    Text(text = stringResource(navItem.name))
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                onClick = {
                    navItem.route.takeIf { it != MainNavigation.Main }?.let {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun NavigationItemContent(
    navItem: BottomNavRoute<out MainNavigation>,
    isSelected: Boolean,
    indicatorWidth: Dp
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(indicatorWidth)
                .height(3.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Icon(
            imageVector = if (isSelected) navItem.selectedIcon else navItem.icon,
            contentDescription = stringResource(navItem.name),
        )
    }
}

@Composable
private fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainActions: MainActions,
    placeViewModel: PlacesViewModel,
    locationViewModel: LocationViewModel,
    recentViewViewModel: RecentViewViewModel,
    permissionResult: Boolean
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainNavigation.Lobby
    ) {
        composable<MainNavigation.Lobby> {
            LobbyScreen(
                onSearchClick = mainActions.onSearchClick,
                onDetailClick = mainActions.onDetailClick,
                placesViewModel = placeViewModel,
                locationViewModel = locationViewModel,
                recentViewViewModel = recentViewViewModel,
                permissionResult = permissionResult,
                onLocationRequestPermission = mainActions.onLocationRequestPermission,
                onCategoryDetailClick = mainActions.onCategoryDetailClick,
                onSeeMoreClick = mainActions.onSeeMoreClick
            )
        }
        composable<MainNavigation.Profile> {
            ProfileScreen(
                onUserLogout = mainActions.onUserLogout,
                onFavoriteClick = mainActions.onFavoriteClick,
                onRatingHistoryClick = mainActions.onHistoryRatingClick,
                onCustomerServiceCallClick = mainActions.onCustomerServiceCallClick,
                onProductRecommendationClick = mainActions.onProductRecommendationClick
            )
        }
    }
}

// Data class untuk menyederhanakan parameter callback
data class MainActions(
    val onSearchClick: () -> Unit,
    val onDetailClick: (placeId: String, placeName: String) -> Unit,
    val onChatAIClick: () -> Unit,
    val onLocationRequestPermission: () -> Unit,
    val onCategoryDetailClick: (name: String, endpoint: String) -> Unit,
    val onSeeMoreClick: () -> Unit,
    val onUserLogout: () -> Unit,
    val onFavoriteClick: () -> Unit,
    val onHistoryRatingClick: () -> Unit,
    val onCustomerServiceCallClick: () -> Unit,
    val onProductRecommendationClick: () -> Unit
)