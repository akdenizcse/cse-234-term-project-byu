import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    var inputText2 by remember { mutableStateOf(TextFieldValue()) }
    var inputText3 by remember { mutableStateOf(TextFieldValue()) }
    

    
    
    
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
        
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),

            value =inputText2 ,
            onValueChange ={inputText2=it},
            label = {Text("Description")}
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { }
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Text(text ="Select Location")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Categories",
            modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            for (category in getAllCategoriez() ){
                chips(category = category.value,
                    onExecuteSearch = {
                    })
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
            repeat(5) {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .width(120.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier
                .height(40.dp)
                .width(120.dp),

            value =inputText3 ,
            onValueChange ={inputText3=it},
            label = {Text("Price")}
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
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
enum class categoriez(val value: String){
    HOME("Home"),
    CLOTHES("Clothes"),
    TECH("Tech"),
    TOYS("Toys"),
    ACCESSORIES("Accessories"),
    COSMETIC("Cosmetic"),
    OTHER("Other")
}

fun getAllCategoriez(): List<categoriez> {
    return listOf(categoriez.HOME,categoriez.CLOTHES,categoriez.TECH,categoriez.TOYS,categoriez.ACCESSORIES,categoriez.COSMETIC,categoriez.OTHER) }

fun getCategoriez(value: String): categoriez?{
    val map= categoriez.values().associateBy(categoriez::value)
    return map[value]
}

@Composable
fun chips(
    category: String,
    onExecuteSearch: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = Color.Gray
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onExecuteSearch(category) })
                .padding(8.dp)
        ) {
            Text(text = category,
                color=Color.White,
                modifier = Modifier.padding(8.dp)
                )
        }
    }
}




