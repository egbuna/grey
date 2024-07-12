package com.example.grey.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavigation = staticCompositionLocalOf<Navigator> { error("Not provided") }

@Stable
class Navigator(
    private val navHostController: NavHostController
) {
    fun navigate(navigationRoute: Navigation) {
        when (navigationRoute) {
            is Navigation.Forward -> {
                navHostController.navigate(navigationRoute.route.path)
            }
            is Navigation.Back -> {
                navHostController.navigateUp()
            }
        }
    }
}

sealed interface Navigation {
    data class Forward(
        val route: Route
    ) : Navigation

    data class Back(val popTo: Route? = null) : Navigation
}
