package com.example.sellstuff

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val messagesCollection = db.collection("messages")

    fun sendMessage(message: Message) {
        messagesCollection.add(message)
    }

    fun getMessages(): Flow<List<Message>> = callbackFlow {
        val listenerRegistration = messagesCollection
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                val messages = snapshot?.documents?.map { it.toObject(Message::class.java)!! }
                trySend(messages ?: emptyList())
            }

        awaitClose { listenerRegistration.remove() }
    }
}

