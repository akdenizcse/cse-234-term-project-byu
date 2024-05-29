package com.example.sellstuff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sellstuff.ui.theme.SellStuffTheme
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellStuffTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirestoreExample()
                }
            }
        }
    }
}

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
            label = { Text("Enter Text") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Upload data to Firestore
                db.collection("exampleCollection")
                    .document("exampleDocument")
                    .set(mapOf("exampleField" to inputText))
                    .addOnSuccessListener {
                        // Fetch data from Firestore
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SellStuffTheme {
        Greeting("Android")
    }
}
