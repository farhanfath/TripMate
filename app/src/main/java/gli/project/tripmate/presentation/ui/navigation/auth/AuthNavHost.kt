package gli.project.tripmate.presentation.ui.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import gli.project.tripmate.presentation.ui.navigation.navitem.AuthNavigation
import gli.project.tripmate.presentation.ui.screen.auth.login.LoginScreen
import gli.project.tripmate.presentation.ui.screen.auth.register.RegisterScreen
import gli.project.tripmate.presentation.ui.screen.auth.splash.SplashScreen
import gli.project.tripmate.presentation.ui.screen.auth.welcome.WelcomeScreen

@Composable
fun AuthNavHost(
    navController: NavHostController,
    onIntentToMain: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthNavigation.Splash
    ) {
        composable<AuthNavigation.Splash> {
            SplashScreen(
                onUserAvailable = {
                    onIntentToMain()
                },
                onUserNotAvailable = {
                    navController.navigate(AuthNavigation.Welcome) {
                        popUpTo(AuthNavigation.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<AuthNavigation.Welcome> {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthNavigation.Login)
                }
            )
        }

        composable<AuthNavigation.Login> {
            LoginScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onNavigateRegister = {
                    navController.navigate(AuthNavigation.Register)
                },
                onLoginSuccess = {
                    onIntentToMain()
                },
                onForgotPass = {

                }
            )
        }
        composable<AuthNavigation.Register> {
            RegisterScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onRegisterSuccess = {
                    navController.navigateUp()
                }
            )
        }
        composable<AuthNavigation.ForgotPassword> {

        }
    }
}