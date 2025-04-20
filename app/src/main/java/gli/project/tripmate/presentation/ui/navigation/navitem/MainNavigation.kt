package gli.project.tripmate.presentation.ui.navigation.navitem

import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavigation {
    @Serializable
    data object Main : MainNavigation()

    @Serializable
    data object Lobby : MainNavigation()

    @Serializable
    data object Favorite : MainNavigation()

    @Serializable
    data object RatingCollection : MainNavigation()

    @Serializable
    data object Profile : MainNavigation()

    @Serializable
    data object ChatAI : MainNavigation()

    @Serializable
    data object Search : MainNavigation()

    @Serializable
    data object Call : MainNavigation()

    /**
     * Detail
     */
    @Serializable
    data class DetailTour(
        val placeId: String,
        val placeName: String
    ) : MainNavigation()

    @Serializable
    data class DetailCategory(
        val categoryName: String,
        val categoryEndpoint: String
    ) : MainNavigation()

    /**
     * more screen
     */
    @Serializable
    data object MoreNearby : MainNavigation()
}