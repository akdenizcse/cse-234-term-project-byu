package com.example.sellstuff

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.tasks.await
import com.google.gson.Gson



@Composable
fun HistoryScreen(navController: NavHostController) {

    var items by remember { mutableStateOf(listOf<Item>()) }
    var selectedCategory by remember { mutableStateOf("Sales") }

    LaunchedEffect(Unit) {
        items = fetchItemsFromFirestore2()
    }

    Scaffold(
        topBar = { TopAppBar2() }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SalesPurchases(selectedCategory) { newCategory ->
                selectedCategory = newCategory
            }
            FilterHeader2()
            ItemList2(items = items.filter { it.category == selectedCategory || selectedCategory == "Sales" }) { item ->
                val itemJson = Uri.encode(Gson().toJson(item))
                navController.navigate("detail/$itemJson")
            }
        }
    }

}



@Composable
fun TopAppBar2() {
    androidx.compose.material.TopAppBar(
        title = { Text(text = "Search in History") },
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
fun FilterHeader2() {
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
fun ItemCard2(item: Item, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .heightIn(min = 250.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize()
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
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Green.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Purchased",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ItemList2(items: List<Item>, onItemClick: (Item) -> Unit) {
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
                    ItemCard2(item = item, modifier = Modifier.weight(1f).clickable { onItemClick(item) })
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun SalesPurchases(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Sales","Purchases")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.Center

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
            if (category != categories.last()) {
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

suspend fun fetchItemsFromFirestore2(): List<Item> {
    val db = FirebaseFirestore.getInstance()
    return try {
        val snapshot = db.collection("products")
            .whereEqualTo("isSaled", true)
            .get()
            .await()
        snapshot.toObjects(Item::class.java)
    } catch (e: Exception) {
        emptyList()
    }
}
