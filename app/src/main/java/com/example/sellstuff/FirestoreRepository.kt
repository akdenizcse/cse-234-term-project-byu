package com.example.sellstuff

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val conversationsCollection = db.collection("conversations")

    fun getConversations(userId: String): Flow<List<Conversation>> = callbackFlow {
        val query = conversationsCollection
            .whereArrayContains("participants", userId)

        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val conversations = snapshot?.documents?.map { document ->
                val id = document.id
                val participants = document.get("participants") as? List<String> ?: emptyList()
                val messages = document.get("messages") as? List<HashMap<String, Any>> ?: emptyList()

                Conversation(
                    id = id,
                    participants = participants,
                    messages = messages.map { it.toMessage() }
                )
            }

            trySend(conversations ?: emptyList())
        }

        awaitClose { listenerRegistration.remove() }
    }

    private fun HashMap<String, Any>.toMessage(): Message {
        return Message(
            text = this["text"] as? String ?: "",
            senderId = this["senderId"] as? String ?: "",
            timestamp = this["timestamp"] as? Long ?: 0
        )
    }

    suspend fun createConversation(participants: List<String>) {
        val newDocRef = conversationsCollection.document()
        val conversation = Conversation(id = newDocRef.id, participants = participants)
        newDocRef.set(conversation).await()
    }
}
