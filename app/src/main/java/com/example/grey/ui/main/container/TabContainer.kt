package com.example.grey.ui.main.container

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.grey.R
import com.example.grey.ui.main.BuildNavGraph
import com.example.grey.ui.navigation.LocalNavigation
import com.example.grey.ui.navigation.Navigator
import com.example.grey.ui.navigation.Root
import com.example.grey.ui.theme.GreyTypography

@Composable
fun TabContainer() {
    val bottomNavRoutes = listOf(Root.Home, Root.Repository, Root.User)

    val navController = rememberNavController()
    val navigator = remember(navController) {
        Navigator(navController)
    }

    var selectedScreen by remember {
        mutableStateOf(Root.Home.path)
    }

    CompositionLocalProvider(LocalNavigation provides navigator) {
        Scaffold(bottomBar = {
            NavigationBar(containerColor = Color.White) {
                bottomNavRoutes.forEach {screen ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == selectedScreen,
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Black,
                            indicatorColor = Color.White
                        ),
                        onClick = {
                                  navController.navigate(screen.path) {
                                      popUpTo(navController.graph.findStartDestination().id) {
                                          saveState = true
                                      }
                                      
                                      launchSingleTop = true
                                      
                                      restoreState = true
                                  }
                            selectedScreen = screen.path
                        },
                        icon = {
                            val icon = when(screen) {
                                Root.User -> if (navController.currentDestination?.route == Root.User.path) R.drawable.user_selected else R.drawable.user
                                Root.Repository -> if (navController.currentDestination?.route == Root.Repository.path) R.drawable.search_selected else R.drawable.search
                                Root.Home -> if (selectedScreen == Root.Home.path) R.drawable.home else R.drawable.home_unselected
                            }
                            
                            Image(painter = painterResource(id = icon), contentDescription = null)
                        },

                        label = {
                            val text = when(screen) {
                                Root.User -> "Users"
                                Root.Repository -> "Repository"
                                Root.Home -> "Home"
                            }

                            Text(text = text, style = GreyTypography.tab, color = Color.Black)
                        })
                }
            }
        }) {
            BuildNavGraph(navController = navController)
        }
    }
}
