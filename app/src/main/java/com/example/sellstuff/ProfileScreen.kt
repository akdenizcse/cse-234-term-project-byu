package com.example.sellstuff

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val user = authViewModel.user.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Email: ${user?.email ?: ""}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { authViewModel.logout() }) {
            Text("Logout")
        }
    }
}
