package gli.project.tripmate.presentation.ui.navigation.navitem

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthNavigation {

    @Serializable
    data object Splash: AuthNavigation()

    @Serializable
    data object Welcome : AuthNavigation()

    @Serializable
    data object Login : AuthNavigation()

    @Serializable
    data object Register : AuthNavigation()

    @Serializable
    data object ForgotPassword : AuthNavigation()
}