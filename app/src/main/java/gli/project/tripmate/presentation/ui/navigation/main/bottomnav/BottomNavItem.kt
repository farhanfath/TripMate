package gli.project.tripmate.presentation.ui.navigation.main.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import gli.project.tripmate.R
import gli.project.tripmate.presentation.ui.navigation.navitem.MainNavigation

data class BottomNavRoute<T : Any>(
    val name: Int,
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

val navItems = listOf(
    BottomNavRoute(
        name = R.string.lobby_label,
        route = MainNavigation.Lobby,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    ),
    BottomNavRoute(
        name = R.string.favorite_label,
        route = MainNavigation.Favorite,
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    ),
    /**
     * placeholder for fab button
     */
    BottomNavRoute(
        name = R.string.ai_chat_label,
        route = MainNavigation.Main,
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    ),
    BottomNavRoute(
        name = R.string.collection_label,
        route = MainNavigation.Collection,
        icon = Icons.Outlined.CollectionsBookmark,
        selectedIcon = Icons.Filled.CollectionsBookmark
    ),
    BottomNavRoute(
        name = R.string.profile_label,
        route = MainNavigation.Profile,
        icon = Icons.Outlined.PersonOutline,
        selectedIcon = Icons.Filled.Person
    )
)