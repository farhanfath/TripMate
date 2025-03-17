package gli.project.tripmate.presentation.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation
import gli.project.tripmate.presentation.ui.navigation.navitem.navItems
import gli.project.tripmate.presentation.ui.screen.main.favorite.FavoriteScreen
import gli.project.tripmate.presentation.ui.screen.main.lobby.LobbyScreen
import gli.project.tripmate.presentation.ui.screen.main.profile.ProfileScreen

@Composable
fun MainNavScreen(
    navController: NavHostController = rememberNavController(),
    onDetailClick: () -> Unit
) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        icon = {
                            if (selectedItemIndex == index) {
                                Icon(
                                    imageVector = navItem.selectedIcon,
                                    contentDescription = stringResource(navItem.name),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = stringResource(navItem.name),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(navItem.name)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        ),
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
                    onDetailClick = onDetailClick
                )
            }
            composable<MainNavigation.Favorite> {
                FavoriteScreen()
            }
            composable<MainNavigation.Profile> {
                ProfileScreen()
            }
        }
    }
}