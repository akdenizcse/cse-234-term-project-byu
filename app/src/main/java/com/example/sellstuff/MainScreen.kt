package com.example.sellstuff

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.sellstuff.ui.theme.SellStuffTheme


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
            startDestination = "firestore",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("firestore") { FirestoreExample() }
            composable("dummy") { DummyScreen() }
            composable("profile") { ProfileScreen(authViewModel) }
        }
    }
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("Firestore", "firestore", Icons.Default.Home),
    BottomNavItem("Dummy", "dummy", Icons.Default.ShoppingCart),
    BottomNavItem("Profile", "profile", Icons.Default.Person)
)