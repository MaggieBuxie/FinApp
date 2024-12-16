package com.example.finapp.ui.theme.Groceries


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.R

data class GroceryItem(
    val id: String,
    val name: String,
    val imageRes: Int,
    val price: String
)

@Composable
fun GroceryShoppingScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf<GroceryItem?>(null) }
    val groceries = listOf(
        GroceryItem("1", "Apples", R.drawable.ic_apples, "$2.99"),
        GroceryItem("2", "Bananas", R.drawable.ic_bananas, "$1.49"),
        GroceryItem("3", "Carrots", R.drawable.ic_carrots, "$3.49")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Search Bar
        SearchBar()

        Spacer(modifier = Modifier.height(20.dp))

        // Active Item Display
        selectedItem?.let { item ->
            ActiveItemDisplay(item = item)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horizontal Item List
        Text(
            text = "Available Groceries",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(groceries.size) { index ->
                GroceryCard(item = groceries[index], onClick = { selectedItem = groceries[index] })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment and Delivery Options
        selectedItem?.let { item ->
            PaymentAndDeliveryOptions(item = item, navController = navController)
        }
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search for groceries...") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { /* Handle search logic here */ }) {
            Text("Search")
        }
    }
}

@Composable
fun ActiveItemDisplay(item: GroceryItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = item.price, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun GroceryCard(item: GroceryItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PaymentAndDeliveryOptions(item: GroceryItem, navController: NavController) {
    var isDeliveryChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Proceed with purchasing ${item.name}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Payment Buttons
        Text("Select Payment Option:")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Handle Credit Card Payment Logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Pay via Credit Card")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Handle PayPal Payment Logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text("Pay via PayPal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delivery Option
        Text("Do you want home delivery?")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Delivery: ")
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = isDeliveryChecked,
                onCheckedChange = { isDeliveryChecked = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Purchase Button
        Button(
            onClick = {
                // Handle Purchase Confirmation
                if (isDeliveryChecked) {
                    // Handle Delivery
                }
                // Handle Payment
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Confirm Purchase")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroceryShoppingScreenPreview() {
    GroceryShoppingScreen(navController = rememberNavController())
}
