import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FirestoreExample() {
    var inputText by remember { mutableStateOf(TextFieldValue()) }
    val db = FirebaseFirestore.getInstance()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

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
                focusManager.clearFocus()
                db.collection("exampleCollection")
                    .document("exampleDocument")
                    .get()
                    .addOnSuccessListener { document ->
                        val currentList = document?.get("exampleField") as? ArrayList<String> ?: arrayListOf()
                        currentList.add(inputText.text)
                        val data = hashMapOf(
                            "exampleField" to currentList
                        )
                        db.collection("exampleCollection")
                            .document("exampleDocument")
                            .set(data)
                            .addOnSuccessListener {
                                // Show toast message
                                CoroutineScope(Dispatchers.Main).launch {
                                    Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle error
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle error
                    }
            }
        ) {
            Text("Upload")
        }
    }
}