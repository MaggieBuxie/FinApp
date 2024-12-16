package com.example.realestateapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finapp.R
import com.example.finapp.ui.theme.FinAppTheme

// Data model to represent each house
data class House(
    val id: String,
    val name: String,
    val location: String,
    val price: String,
    val imagesRes: List<Int> // Multiple images for sliding effect
)

@Composable
fun RealEstateScreen(navController: NavController, isAdmin: Boolean) {
    var houses by remember {
        mutableStateOf(
            listOf(
                House("1", "Beautiful Villa", "California", "$500,000", listOf(R.drawable.ic_house1, R.drawable.ic_house2)),
                House("2", "Modern Apartment", "New York", "$300,000", listOf(R.drawable.ic_house3, R.drawable.ic_house4)),
                House("3", "Beachfront Property", "Miami", "$750,000", listOf(R.drawable.ic_house5, R.drawable.ic_house6)),
                House("4", "Mambo Villa", "California", "$500,000", listOf(R.drawable.ic_house7, R.drawable.ic_house8)),
                House("5", "Serene Apartment", "New York", "$300,000", listOf(R.drawable.ic_house9, R.drawable.ic_house10)),
                House("6", "Holiday Paradise", "Miami", "$750,000", listOf(R.drawable.ic_house11, R.drawable.ic_house12))
            )
        )
    }

    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                keyboardActions = KeyboardActions(onDone = { /* Perform search logic */ }),
                maxLines = 1
            )
            IconButton(onClick = { navController.navigate("home_screen") }) {
                Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Back to Home")
            }
        }

        // List of houses
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(houses.size) { index ->
                HouseCard(house = houses[index], onViewClick = { houseId ->
                    navController.navigate("view_house/$houseId")
                }, onPaymentClick = { houseId ->
                    navController.navigate("payment_screen/$houseId")
                })
            }
        }
    }
}

@Composable
fun HouseCard(house: House, onViewClick: (String) -> Unit, onPaymentClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = house.imagesRes.first()),
                contentDescription = house.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = house.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = house.location, style = MaterialTheme.typography.bodySmall)
                Text(text = house.price, style = MaterialTheme.typography.bodyLarge)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { onViewClick(house.id) }) {
                    Text("View")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onPaymentClick(house.id) }) {
                    Text("Make Payment")
                }
            }
        }
    }
}

// View house with slides
@Composable
fun ViewHouseScreen(houseId: String, navController: NavController) {
    val house = getHouseById(houseId) // Fetch the house details based on ID

    var currentImageIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(painter = painterResource(id = R.drawable.logo3), contentDescription = "Back")
        }

        Text(
            text = house?.name ?: "Unknown House",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Image(
            painter = painterResource(id = house?.imagesRes?.get(currentImageIndex) ?: R.drawable.ic_house1),
            contentDescription = house?.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (currentImageIndex > 0) currentImageIndex--
            }) {
                Icon(painter = painterResource(id = R.drawable.logo1), contentDescription = "Previous Image")
            }

            IconButton(onClick = {
                if (house != null && currentImageIndex < house.imagesRes.size - 1) {
                    currentImageIndex++
                }
            }) {
                Icon(painter = painterResource(id = R.drawable.logo2), contentDescription = "Next Image")
            }
        }
    }
}

// Simulate fetching house by ID
fun getHouseById(id: String): House? {
    // Replace this with your actual data fetching mechanism (e.g., API call or database query)
    return House("1", "Beautiful Villa", "California", "$500,000", listOf(R.drawable.ic_house1, R.drawable.ic_house2))
}

@Composable
fun PaymentScreen(houseId: String, navController: NavController) {
    val house = getHouseById(houseId)

    // Here, you can add the payment UI and logic
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Payment for ${house?.name ?: "Unknown House"}", style = MaterialTheme.typography.headlineMedium)
        // Payment button, input, etc.
        Button(onClick = { navController.popBackStack() }) {
            Text("Confirm Payment")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RealEstateScreenPreview() {
    FinAppTheme() {
        val navController = rememberNavController()
        RealEstateScreen(navController, isAdmin = false)
    }
}

@Composable
fun MainActivity() {
    val navController = rememberNavController()

    // Navigation setup
    NavHost(navController = navController, startDestination = "real_estate_screen") {
        composable("real_estate_screen") {
            RealEstateScreen(navController, isAdmin = false)
        }
        composable("view_house/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            ViewHouseScreen(houseId = houseId, navController = navController)
        }
        composable("payment_screen/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            PaymentScreen(houseId = houseId, navController = navController)
        }
    }
}
