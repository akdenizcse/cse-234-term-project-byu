package com.example.sellstuff

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMessage by authViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.clearErrorMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Login", modifier = Modifier
                .fillMaxWidth()
                .size(80.dp), fontSize = 50.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Forgot your password?",
            modifier = Modifier.padding(start = 180.dp).clickable {  },
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.login(email, password)
                } else {
                    // Show error message for empty fields
                    authViewModel.clearErrorMessage()
                    authViewModel.setError("Please enter email and password.")
                }
            }
        ) {
            Text("Login")
        }
        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign up")
        }

        errorMessage?.let {
            Text(text = it, color = Color.Red)
        }
    }
}

@Composable
fun SignupScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMessage by authViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.clearErrorMessage()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.signup(email, password)
                } else {
                    // Show error message for empty fields
                    authViewModel.clearErrorMessage()
                    authViewModel.setError("Please enter email and password.")
                }
            }
        ) {
            Text("Sign Up")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Login")
        }

        errorMessage?.let {
            Text(text = it, color = Color.Red)
        }
    }
}
