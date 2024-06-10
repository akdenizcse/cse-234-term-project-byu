package com.example.sellstuff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MessagingViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages

    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun getMessages(conversationId: String) {
        db.collection("conversations").document(conversationId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val messages = snapshot?.get("messages") as? List<HashMap<String, Any>> ?: emptyList()
                _messages.value = messages.map { it.toMessage() }
            }
    }

    fun sendMessage(conversationId: String, text: String) {
        val message = Message(
            text = text,
            senderId = currentUserId,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            db.collection("conversations").document(conversationId)
                .update("messages", FieldValue.arrayUnion(message.toMap()))
        }
    }

    private fun HashMap<String, Any>.toMessage(): Message {
        return Message(
            text = this["text"] as? String ?: "",
            senderId = this["senderId"] as? String ?: "",
            timestamp = this["timestamp"] as? Long ?: 0
        )
    }

    private fun Message.toMap(): HashMap<String, Any> {
        return hashMapOf(
            "text" to text,
            "senderId" to senderId,
            "timestamp" to timestamp
        )
    }
}