package com.example.sellstuff

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XF9F9F9))
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            Text(
                text = "Login", modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp), fontSize = 40.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    cursorColor = Color.Black, unfocusedContainerColor = Color.White
                ),
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    cursorColor = Color.Black, unfocusedContainerColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Forgot your password?",
                modifier = Modifier
                    .padding(start = 180.dp)
                    .clickable { },
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                Text("LOGIN")
            }
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Don't have an account? Sign up", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(140.dp))
            Text(
                text = "Or login with social account",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(100.dp, 80.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(10.dp))
                        .border(3.dp, color = Color.White, shape = RoundedCornerShape(10.dp)),

                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.White
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.googleicon),
                        contentDescription = "googleicon",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(100.dp, 80.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(10.dp))
                        .border(3.dp, color = Color.White, shape = RoundedCornerShape(10.dp)),

                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.White
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebookicon),
                        contentDescription = "googleicon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
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
