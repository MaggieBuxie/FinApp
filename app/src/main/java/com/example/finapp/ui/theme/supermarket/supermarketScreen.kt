package com.example.finapp.ui.theme.supermarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finapp.R

// Data class to represent a product
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int
)

// Data class to represent a cart item
data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)

@Composable
fun SupermarketScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var cart by remember { mutableStateOf<List<CartItem>>(emptyList()) }

    // Sample product list
    val products = List(20) { index ->
        Product(
            id = index,
            name = "Product ${index + 1}",
            price = (10..100).random().toDouble(),
            imageRes = R.drawable.ic_sample_product // Replace with actual product images
        )
    }

    Scaffold(
        topBar = {
            SearchBar(searchQuery) { query -> searchQuery = query }
        },
        bottomBar = {
            BottomAppBar {
                Button(onClick = { navController.navigate("checkout") }) {
                    Text("Go to Checkout (${cart.size} items)")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val filteredProducts = products.filter { it.name.contains(searchQuery, ignoreCase = true) }
                items(filteredProducts.size) { index ->
                    val product = filteredProducts[index]
                    ProductCard(product = product, onAddToCart = {
                        cart = cart.toMutableList().apply {
                            val existingItem = find { it.id == product.id }
                            if (existingItem != null) {
                                existingItem.quantity += 1
                            } else {
                                add(CartItem(product.id, product.name, product.price, 1))
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onAddToCart() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onAddToCart() }) {
                Text("Add to Cart")
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { onQueryChange("") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = "Clear search"
            )
        }
    }
}
