package com.example.sellstuff

import com.example.sellstuff.ui.theme.SellStuffTheme

@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sellstuff.ui.theme.SellStuffTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellStuffTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(items = listOf(
                            BottomNavigationItem(
                                name = "Home",
                                route = "home",
                                icon = Icons.Default.Home

                            ),
                            BottomNavigationItem(
                                name = "Chat",
                                route = "chat",
                                icon = Icons.Default.Notifications

                            ),
                            BottomNavigationItem(
                                name = "Upload",
                                route = "add",
                                icon = Icons.Default.Add

                            ),
                            BottomNavigationItem(
                                name = "History",
                                route = "history",
                                icon = Icons.Default.ArrowBack

                            ),
                            BottomNavigationItem(
                                name = "Profile",
                                route = "profile",
                                icon = Icons.Default.AccountCircle

                            ),
                        ),
                            navController = navController ,
                            onItemClick = {
                                navController.navigate(it.route)
                            } )
                    }
                ) {
                    Navigation(navController=navController)
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    navController: NavController,
    modifier: Modifier=Modifier,
    onItemClick: (BottomNavigationItem) -> Unit
){
    val backStackEntry=NavController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = Modifier,
        containerColor = Color.LightGray,
        tonalElevation = 5.dp
    ) {
        items.forEach{
                item ->
            val selected= item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {onItemClick(item)},
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color .Green,
                    unselectedIconColor = Color.Gray
                ),
                icon = {
                    Column(horizontalAlignment = CenterHorizontally){
                        if(item.badgeCount>0){
                            BadgedBox(badgedContent = {
                                Text(text=item.badgeCount.toString())
                            }
                            ) {
                                Icon(ImageVector =item.icon , contentDescription = item.name )


                            }
                        }
                        else {
                            Icon(ImageVector =item.icon , contentDescription = item.name )
                        }
                        if (selected){
                            Text(text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                })
        }

    }

}


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home" ) {
        composable("home"){
            HomeScreen()
        }
        composable("chat"){
            ChatScreen()
        }
        composable("add"){
            AddScreen()
        }
        composable("history"){
            HistoryScreen()
        }
        composable("profile"){
            ProfileScreen()
        }
    }
}

@Composable
fun HomeScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Home Screen")
    }
}

@Composable
fun ChatScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Chat Screen")
    }
}

@Composable
fun AddScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Upload Screen")
    }
}

@Composable
fun HistoryScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "History Screen")
    }
}

@Composable
fun ProfileScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Profile Screen")
    }
}