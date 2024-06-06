package com.example.sellstuff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MessagingScreen(viewModel: MessagingViewModel) {
    val messages by viewModel.messages.collectAsState()
    var currentMessage by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = if (message.senderId == currentUser?.uid) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .background(if (message.senderId == currentUser?.uid) Color.Gray else Color.LightGray)
                            .padding(16.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter message") }
            )
            Button(
                onClick = {
                    if (currentMessage.isNotEmpty()) {
                        currentUser?.let {
                            viewModel.sendMessage(
                                Message(
                                    text = currentMessage,
                                    senderId = it.uid
                                )
                            )
                        }
                        currentMessage = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
