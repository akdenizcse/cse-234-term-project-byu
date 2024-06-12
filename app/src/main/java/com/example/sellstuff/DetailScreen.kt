package com.example.sellstuff

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.gson.Gson

@Composable
fun DetailScreen(navController: NavHostController, itemJson: String?) {
    val item = itemJson?.let { Gson().fromJson(it, Item::class.java) }
    if (item == null) {
        androidx.compose.material3.Text(
            "Item not found",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else {
        androidx.compose.material3.Scaffold(
            topBar = {
                androidx.compose.material.TopAppBar(
                    title = { androidx.compose.material3.Text(text = item.title) },
                    navigationIcon = {
                        androidx.compose.material3.IconButton(onClick = { navController.popBackStack() }) {
                            androidx.compose.material3.Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    backgroundColor = Color.White,
                    actions = {
                        androidx.compose.material3.IconButton(onClick = { /* TODO */ }) {
                            androidx.compose.material3.Icon(
                                Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
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
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material3.Text(
                    text = item.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Text(text = item.description)
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.Text(
                    text = "Price: $${item.price}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
