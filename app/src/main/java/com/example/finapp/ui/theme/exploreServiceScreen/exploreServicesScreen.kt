package com.example.finapp.ui.theme.exploreServiceScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.R

// Service data model
data class Service(
    val name: String,
    val iconRes: Int,
    val imageRes: Int // Added image for the product picture
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreServicesScreen(navController: NavController) {
    // List of services to explore
    val services = listOf(
        Service("Buy Cars", R.drawable.car_image, R.drawable.car_image),
        Service("Sell Cars", R.drawable.car_image, R.drawable.car_image),
        Service("Real Estate", R.drawable.house_image, R.drawable.house_image),
        Service("Groceries", R.drawable.groceries_image, R.drawable.groceries_image),
        Service("Supermarket", R.drawable.supermarket_image, R.drawable.supermarket_image),
        Service("Use Your Visa", R.drawable.visa_image, R.drawable.visa_image),
        Service("Book Hotels/Airbnb", R.drawable.hotel_image, R.drawable.hotel_image),
        Service("Order Food", R.drawable.food_image, R.drawable.food_image),
        Service("Order Uber", R.drawable.uber_image, R.drawable.uber_image),
        Service("Book a Safari", R.drawable.safari_image, R.drawable.safari_image)
    )

    // Search query state
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            // TopAppBar with Search bar
            TopAppBar(
                title = { Text("Explore Services") },
                actions = {
                    SearchBar(searchQuery) { searchQuery = it }
                }
            )
        }
    ) { padding ->
        // Main content
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Service Grid
            ServiceGrid(
                services = services.filter { service ->
                    service.name.contains(searchQuery.text, ignoreCase = true)
                },
                onServiceClick = { service ->
                    navigateToService(navController, service)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: TextFieldValue, onSearchQueryChange: (TextFieldValue) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .padding(8.dp),
        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
    )
}

@Composable
fun ServiceGrid(services: List<Service>, onServiceClick: (Service) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(services) { service ->
            ServiceCard(service = service, onClick = { onServiceClick(service) })
        }
    }
}

@Composable
fun ServiceCard(service: Service, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Product Image
            ProductImage(service.imageRes)

            // Service Icon
            ServiceIcon(service.iconRes)

            // Service Name
            ServiceName(service.name)
        }
    }
}

@Composable
fun ProductImage(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun ServiceIcon(iconRes: Int) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier.size(48.dp)
    )
}

@Composable
fun ServiceName(name: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = name, style = MaterialTheme.typography.bodyLarge)
}

fun navigateToService(navController: NavController, service: Service) {
    when (service.name) {
        "Buy Cars" -> navController.navigate("buy_cars")
        "Sell Cars" -> navController.navigate("sell_cars")
        "Real Estate" -> navController.navigate("real_estate")
        "Groceries" -> navController.navigate("groceries")
        "Supermarket" -> navController.navigate("supermarket")
        "Use Your Visa" -> navController.navigate("use_visa")
        "Book Hotels/Airbnb" -> navController.navigate("book_hotels_airbnb")
        "Order Food" -> navController.navigate("order_food")
        "Order Uber" -> navController.navigate("order_uber")
        "Book a Safari" -> navController.navigate("book_a_safari")
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreServicesScreenPreview() {
    ExploreServicesScreen(navController = rememberNavController())
}
