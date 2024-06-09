package com.example.sellstuff

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ConversationScreen(viewModel: ConversationViewModel) {
    val conversations by viewModel.conversations.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid ?: ""

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Conversations",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                if (conversations.isEmpty()) {
                    item {
                        Text(
                            text = "No conversations found.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(conversations) { conversation ->
                        ConversationItem(conversation)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val newParticipants = listOf(currentUserId, "user2") // Current user and hardcoded user
                    viewModel.createConversation(newParticipants)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Create New Conversation")
            }
        }
    }

    // Fetch conversations when the screen is displayed
    LaunchedEffect(currentUserId) {
        viewModel.getConversations(currentUserId)
    }
}

@Composable
fun ConversationItem(conversation: Conversation) {
    val participants = conversation.participants.joinToString(", ")
    val lastMessage = conversation.messages.lastOrNull()?.text ?: "No messages yet"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to detailed conversation screen */ }
            .padding(16.dp)
    ) {
        Text(text = "Participants: $participants", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Last message: $lastMessage", style = MaterialTheme.typography.bodyMedium)
    }
}