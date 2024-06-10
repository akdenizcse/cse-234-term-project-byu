package com.example.sellstuff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConversationViewModel(private val repository: FirestoreRepository) : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations

    fun getConversations(currentUserId: String) {
        viewModelScope.launch {
            repository.getConversations(currentUserId).collect { convos ->
                _conversations.value = convos
            }
        }
    }

    fun createConversation(participants: List<String>) {
        viewModelScope.launch {
            repository.createConversation(participants)
        }
    }
}

class ConversationViewModelFactory(
    private val repository: FirestoreRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationViewModel::class.java)) {
            return ConversationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
