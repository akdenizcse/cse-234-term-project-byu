package com.example.sellstuff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Composable
fun ConversationScreen(navController: NavHostController, email: String, viewModel: ConversationViewModel = viewModel()) {
    val conversations by viewModel.conversations.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid ?: ""
    var isConversationCreated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (email.isNotEmpty() && !isConversationCreated) {
            val newParticipants = listOf(currentUserId, email)
            viewModel.createConversation(newParticipants)
            isConversationCreated = true
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Messages",
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
                            ConversationItem(conversation, currentUserId, navController)
                        }
                    }
                }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Fetch conversations when the screen is displayed
    LaunchedEffect(currentUserId) {
        viewModel.getConversations(currentUserId)
    }
}

@Composable
fun ConversationItem(conversation: Conversation, currentUserId: String, navController: NavHostController) {
    val participants = conversation.participants.filter { it != currentUserId }
    val lastMessage = conversation.messages.lastOrNull()?.text ?: "No messages yet"
    val timestamp = conversation.messages.lastOrNull()?.timestamp ?: System.currentTimeMillis()
    val timeAgo = getTimeAgo(timestamp)
    val emailMap = remember { mutableMapOf<String, String>() }

    // Fetch and store participant emails
    LaunchedEffect(participants) {
        participants.forEach { userId ->
            getUserEmail(userId).collect { email ->
                email?.let {
                    emailMap[userId] = it
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("messaging/${conversation.id}")
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(55.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                tint = Color.White,
                modifier = Modifier.size(35.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .height(55.dp)
        ) {
            participants.forEach { userId ->
                Text(
                    text = emailMap[userId] ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
            Text(
                text = lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Text(
            text = timeAgo,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

fun getTimeAgo(timestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val timeDifferenceMillis = currentTime - timestamp

    val seconds = timeDifferenceMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(timestamp))
            formattedDate
        }
        hours > 0 -> "$hours h ago"
        minutes > 0 -> "$minutes min ago"
        else -> "Just now"
    }
}

fun getUserEmail(userId: String): Flow<String?> = callbackFlow {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")
    val userDoc = usersCollection.document(userId)

    val listenerRegistration = userDoc.addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }

        val email = snapshot?.getString("email")
        // Emit the email to the flow
        trySend(email)
            .onSuccess { }
            .onFailure { close(it) }
    }

    awaitClose { listenerRegistration.remove() }
}
