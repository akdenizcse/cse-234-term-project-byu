package com.example.sellstuff

data class Message(
    val text: String = "",
    val senderId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Conversation(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val messages: List<Message> = emptyList()
)