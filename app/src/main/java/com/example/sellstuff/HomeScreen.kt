package com.example.sellstuff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen() {
    var text by remember { mutableStateOf("Loading...") }

    // Fetch data automatically when HomeScreen is first composed
    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("exampleCollection")
            .document("exampleDocument")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val exampleField = document.get("exampleField") as? List<String>
                    text = exampleField?.joinToString("\n") ?: "No Data"
                } else {
                    text = "No Document Found"
                }
            }
            .addOnFailureListener { exception ->
                text = "Error: ${exception.message}"
            }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
    }
}
