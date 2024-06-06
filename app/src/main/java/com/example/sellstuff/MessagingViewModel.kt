package com.example.sellstuff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MessagingViewModel : ViewModel() {
    private val repository = FirestoreRepository()
    private val auth = FirebaseAuth.getInstance()

    val messages: StateFlow<List<Message>> = repository.getMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendMessage(message: Message) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val messageWithUserId = message.copy(senderId = currentUser.uid)
            repository.sendMessage(messageWithUserId)
        }
    }
}

