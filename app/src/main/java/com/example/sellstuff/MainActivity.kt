package com.example.sellstuff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.sellstuff.ui.theme.SellStuffTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellStuffTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { FirestoreExample() }
            composable("messages") { MessageScreen() }
            composable("add"){ AddScreen()}
            composable("history"){ HistoryScreen()}
            composable("profile"){ ProfileScreen()}
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val items = listOf(
            BottomNavItem(title = "Homepage", route = "home", icon = Icons.Default.Home),
            BottomNavItem(title = "Messages", route = "messages", icon = Icons.Default.Notifications),
            BottomNavItem(title = "Add", route = "add", icon = Icons.Default.Add),
            BottomNavItem(title = "History", route = "history", icon = Icons.Default.List),
            BottomNavItem(title = "Profile", route = "profile", icon = Icons.Default.AccountCircle)


        )

        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
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

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector = Icons.Default.Home)

@Composable
fun FirestoreExample() {
    var inputText by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("Loading...") }
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter Text to add product Firebase") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Upload data to Firestore
                db.collection("exampleCollection")
                    .document("exampleDocument")
                    .set(mapOf("exampleField" to inputText))
                    .addOnSuccessListener {
                        // Fetch data from Firestore
                        db.collection("exampleCollection")
                            .document("exampleDocument")
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    text = document.getString("exampleField") ?: "No Data"
                                } else {
                                    text = "No Document Found"
                                }
                            }
                            .addOnFailureListener { exception ->
                                text = "Error: ${exception.message}"
                            }
                    }
                    .addOnFailureListener { exception ->
                        text = "Error: ${exception.message}"
                    }
            }
        ) {
            Text("Upload & Fetch")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text)
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is your profile.")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SellStuffTheme {
        Greeting("Android")
    }
}
@Composable
fun MessageScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is chat screen")
    }
}
@Composable
fun AddScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is uploading screen.")
    }
}

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is history screen.")
    }
}
