package com.example.sellstuff

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FirestoreExample() {
    var inputText by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("Loading...") }
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter Text to add product Firebase") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                db.collection("exampleCollection")
                    .document("exampleDocument")
                    .set(mapOf("exampleField" to inputText))
                    .addOnSuccessListener {
                        db.collection("exampleCollection")
                            .document("exampleDocument")
                            .get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    text = document.getString("exampleField") ?: "No Data"
                                } else {
                                    text = "No Document Found"
                                }
                            }
                            .addOnFailureListener { exception ->
                                text = "Error: ${exception.message}"
                            }
                    }
                    .addOnFailureListener { exception ->
                        text = "Error: ${exception.message}"
                    }
            }
        ) {
            Text("Upload & Fetch")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text)
    }
}

@Composable
fun DummyScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("This is a dummy screen.")
    }
}