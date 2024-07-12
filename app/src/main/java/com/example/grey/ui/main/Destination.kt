package com.example.grey.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.grey.ui.home.HomePage
import com.example.grey.ui.navigation.Root
import com.example.grey.ui.navigation.UserRoute
import com.example.grey.ui.repository.RepositoryPage
import com.example.grey.ui.user.UserPage
import com.example.grey.ui.user.detail.UserDetailPage

@Composable
fun BuildNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Root.Home.path
    ) {

        composable(Root.Home.path) {
            HomePage()
        }

        composable(Root.Repository.path) {
            RepositoryPage()
        }

        composable(Root.User.path) {
            UserPage()
        }

        composable(UserRoute.UserDetail.buildPath(),
            arguments = listOf(
                navArgument(UserRoute.UserDetail.ARG_USERNAME) {
                    type = NavType.StringType
                }
            )
        ) {
            UserDetailPage()
        }
    }
}
