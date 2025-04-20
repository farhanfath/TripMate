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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    onSearchClick: () -> Unit,
    onDetailClick: (placeId: String, placeName: String) -> Unit,
    onChatAIClick: () -> Unit,
    placeViewModel: PlacesViewModel,
    locationViewModel: LocationViewModel,
    recentViewViewModel: RecentViewViewModel,
    permissionResult: Boolean,
    onLocationRequestPermission: () -> Unit,
    onCategoryDetailClick: (name: String, endpoint: String) -> Unit,
    onSeeMoreClick: () -> Unit,
    onUserLogout: () -> Unit,
    onFavoriteClick: () -> Unit,
    onHistoryRatingClick: () -> Unit,
    onCustomerServiceCallClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(70.dp)
                    .offset(y = 50.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(4.dp),
                onClick = onChatAIClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .clip(
                        BottomNavShape(
                            cornerRadius = with(LocalDensity.current) { 10.dp.toPx() },
                            dockRadius = with(LocalDensity.current) { 45.dp.toPx() },
                        )
                    ),
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                navItems.forEach { navItem ->
                    val selectedBarItem = currentDestination?.hierarchy?.any { it.hasRoute(navItem.route::class) } == true
                    val indicatorWidth by animateDpAsState(
                        targetValue = if (selectedBarItem) 24.dp else 0.dp,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 600f)
                    )
                    NavigationBarItem(
                        selected = selectedBarItem,
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
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
                                if (selectedBarItem) {
                                    Icon(
                                        imageVector = navItem.selectedIcon,
                                        contentDescription = stringResource(navItem.name),
                                    )
                                } else {
                                    Icon(
                                        imageVector = navItem.icon,
                                        contentDescription = stringResource(navItem.name),
                                    )
                                }
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(navItem.name)
                            )
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
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = MainNavigation.Lobby
        ) {
            composable<MainNavigation.Lobby> {
                LobbyScreen(
                    onSearchClick = onSearchClick,
                    onDetailClick = { placeId, placeName ->
                        onDetailClick(placeId, placeName)
                    },
                    placesViewModel = placeViewModel,
                    locationViewModel = locationViewModel,
                    recentViewViewModel = recentViewViewModel,
                    permissionResult = permissionResult,
                    onLocationRequestPermission = onLocationRequestPermission,
                    onCategoryDetailClick = { name, endpoint ->
                        onCategoryDetailClick(name, endpoint)
                    },
                    onSeeMoreClick = onSeeMoreClick
                )
            }
            composable<MainNavigation.Profile> {
                ProfileScreen(
                    onUserLogout = {
                        onUserLogout()
                    },
                    onFavoriteClick = {
                        onFavoriteClick()
                    },
                    onRatingHistoryClick = {
                        onHistoryRatingClick()
                    },
                    onCustomerServiceCallClick = {
                        onCustomerServiceCallClick()
                    }
                )
            }
        }
    }
}