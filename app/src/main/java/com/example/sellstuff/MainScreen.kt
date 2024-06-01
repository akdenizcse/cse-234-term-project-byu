package com.example.sellstuff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val user by authViewModel.user.collectAsState()

    if (user == null) {
        AuthNavHost(navController = navController, authViewModel = authViewModel)
    } else {
        AppNavHost(navController = navController, authViewModel = authViewModel)
    }
}

@Composable
fun AuthNavHost(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("signup") { SignupScreen(navController, authViewModel) }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, authViewModel: AuthViewModel) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("messages") { MessageScreen() }
            composable("add") { FirestoreExample()}
            composable("history") { HistoryScreen()}
            composable("profile") { ProfileScreen(authViewModel) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem(title = "Homepage", route = "home", icon = Icons.Default.Home),
    BottomNavItem(title = "Messages", route = "messages", icon = Icons.Default.Send),
    BottomNavItem(title = "Add", route = "add", icon = Icons.Default.Add),
    BottomNavItem(title = "History", route = "history", icon = Icons.Default.Done),
    BottomNavItem(title = "Profile", route = "profile", icon = Icons.Default.AccountCircle)
)

