import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

@Composable
fun FirestoreExample() {
    var inputText by remember { mutableStateOf(TextFieldValue()) }
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var inputText2 by remember { mutableStateOf(TextFieldValue()) }
    var inputText3 by remember { mutableStateOf(TextFieldValue()) }
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val ownerID = currentUser?.uid

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uriList ->
            if (uriList.isNotEmpty()) {
                selectedImages = uriList
            } else {
                Toast.makeText(context, "No images selected", Toast.LENGTH_SHORT).show()
            }
        }
    )

    var selectedCategory by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ADD",
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Upload Products to Sell",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Title") }
            )

            TextField(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp),
                value = inputText3,
                onValueChange = { inputText3 = it },
                label = { Text("Price") }
            )
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            value = inputText2,
            onValueChange = { inputText2 = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable { }
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Select Location")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Categories", modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            for (category in getAllCategoriez()) {
                chips(
                    category = category.value,
                    isSelected = selectedCategory == category.value,
                    onExecuteSearch = {
                        selectedCategory = category.value
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Add Photos",
            modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedImages.forEach { uri ->
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .width(120.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .width(120.dp)
                    .background(Color.LightGray)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        val coroutineScope = rememberCoroutineScope()

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            onClick = {
                focusManager.clearFocus()
                if (ownerID != null) {
                    coroutineScope.launch {
                        val imageUrls = uploadImagesToStorage(selectedImages, storage)
                        val productData = hashMapOf(
                            "title" to inputText.text,
                            "description" to inputText2.text,
                            "price" to inputText3.text,
                            "images" to imageUrls,
                            "ownerID" to ownerID,
                            "category" to selectedCategory,
                            "isSaled" to false
                        )
                        db.collection("products")
                            .add(productData)
                            .addOnSuccessListener {
                                // Clear the fields
                                inputText = TextFieldValue()
                                inputText2 = TextFieldValue()
                                inputText3 = TextFieldValue()
                                selectedImages = listOf()
                                selectedCategory = ""
                                Toast.makeText(context, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to upload product", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Upload")
        }
    }
}

private suspend fun uploadImagesToStorage(selectedImages: List<Uri>, storage: FirebaseStorage): List<String> {
    val imageUrls = mutableListOf<String>()
    for (uri in selectedImages) {
        val storageRef = storage.reference.child("images/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(uri).await()
        val downloadUrl = uploadTask.storage.downloadUrl.await()
        imageUrls.add(downloadUrl.toString())
    }
    return imageUrls
}

enum class categoriez(val value: String) {
    HOME("Home"),
    CLOTHES("Clothes"),
    TECH("Tech"),
    TOYS("Toys"),
    ACCESSORIES("Accessories"),
    COSMETIC("Cosmetic"),
    OTHER("Other")
}

fun getAllCategoriez(): List<categoriez> {
    return listOf(
        categoriez.HOME, categoriez.CLOTHES, categoriez.TECH, categoriez.TOYS, categoriez.ACCESSORIES, categoriez.COSMETIC, categoriez.OTHER
    )
}

@Composable
fun chips(
    category: String,
    isSelected: Boolean,
    onExecuteSearch: (String) -> Unit,
) {
    val backgroundColor = if (isSelected) Color.Blue else Color.Gray
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onExecuteSearch(category) })
                .padding(8.dp)
        ) {
            Text(
                text = category,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
