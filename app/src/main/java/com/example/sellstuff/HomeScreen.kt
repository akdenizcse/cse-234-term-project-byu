package com.example.sellstuff

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(navController: NavHostController) {
    var items by remember { mutableStateOf(listOf<Item>()) }
    var selectedCategory by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        items = fetchItemsFromFirestore()
    }

    Scaffold(
        topBar = { TopAppBar() }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Categories(selectedCategory) { newCategory ->
                selectedCategory = newCategory
            }
            FilterHeader()
            ItemList(items = items.filter { it.category == selectedCategory || selectedCategory == "All" }) { item ->
                val itemJson = Uri.encode(Gson().toJson(item))
                navController.navigate("detail/$itemJson")
            }
        }
    }
}

@Composable
fun TopAppBar() {
    androidx.compose.material.TopAppBar(
        title = { Text(text = "SellStuff") },
        backgroundColor = Color.White,
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun FilterHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Filter",
                color = Color.Gray,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sort",
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun Categories(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("All", "Home", "Clothes", "Tech", "Toys", "Accessories", "Cosmetic", "Other")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // spacing between items
    ) {
        items(categories) { category ->
            Text(
                text = category,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (category == selectedCategory) Color.Blue else Color.Gray,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onCategorySelected(category) }
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun ItemList(items: List<Item>, onItemClick: (Item) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = 16.dp)
    ) {
        itemsIndexed(items.chunked(2)) { _, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                rowItems.forEach { item ->
                    ItemCard(item = item, modifier = Modifier.weight(1f).clickable { onItemClick(item) })
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: Item, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .heightIn(min = 250.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (item.images.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(item.images[0]),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.title,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "5 km",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = "$${item.price}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class Item(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val images: List<String> = emptyList(),
    val ownerID: String = "",
    val category: String = "",
    val isSaled: Boolean = false
)

suspend fun fetchItemsFromFirestore(): List<Item> {
    return try {
        val db = FirebaseFirestore.getInstance()
        val result = db.collection("products").get().await()
        result.documents.mapNotNull { document ->
            document.toObject(Item::class.java)?.copy(id = document.id)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
