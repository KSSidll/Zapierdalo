package com.kssidll.zapierdalo

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kssidll.zapierdalo.ui.screen.dashboard.DashboardRoute
import com.kssidll.zapierdalo.ui.screen.settings.SettingsRoute
import kotlinx.serialization.Serializable

sealed class NavigationDestinations {
    @Immutable
    @Serializable
    data object Dashboard: NavigationDestinations()

    @Immutable
    @Serializable
    data object Settings: NavigationDestinations()
}

val defaultNavigateEasing = CubicBezierEasing(
    0.48f,
    0.19f,
    0.05f,
    1.03f
)

const val defaultNavigateDurationMilis = 300

fun defaultNavigateEnterTransition(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = defaultNavigateDurationMilis,
            easing = defaultNavigateEasing
        ),
        initialOffsetX = { it }
    )
}

fun defaultNavigatePopEnterTransition(): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = defaultNavigateDurationMilis,
            easing = defaultNavigateEasing
        ),
        initialOffsetX = { -it }
    )
}

fun defaultNavigateExitTransition(): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = defaultNavigateDurationMilis,
            easing = defaultNavigateEasing
        ),
        targetOffsetX = { -it }
    )
}

fun defaultNavigatePopExitTransition(): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = defaultNavigateDurationMilis,
            easing = defaultNavigateEasing
        ),
        targetOffsetX = { it }
    )
}

@Composable
fun Navigation(
    appState: AppState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navigateBack: () -> Unit = {
        navController.popBackStack()
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationDestinations.Dashboard,
        enterTransition = {
            defaultNavigateEnterTransition()
        },
        popEnterTransition = {
            defaultNavigatePopEnterTransition()
        },
        exitTransition = {
            defaultNavigateExitTransition()
        },
        popExitTransition = {
            defaultNavigatePopExitTransition()
        },
    ) {
        composable<NavigationDestinations.Dashboard> {
            DashboardRoute(
                appState = appState,
                navigateSettings = {
                    navController.navigate(NavigationDestinations.Settings)
                }
            )
        }

        composable<NavigationDestinations.Settings> {
            SettingsRoute(
                appState = appState,
                navigateBack = navigateBack,
            )
        }
    }
}

