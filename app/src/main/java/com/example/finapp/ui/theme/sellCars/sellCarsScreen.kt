package com.example.finapp.ui.theme.sellCars

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
import com.example.finapp.navigation.ROUTE_TRANSACTIONS

data class Car(
    val id: String,
    val name: String,
    val imageRes: Int,
    val seller: String,
    val price: String
)

@Composable
fun SellCarsScreen(navController: NavController) {
    var activeCar by remember { mutableStateOf<Car?>(null) }
    val cars = listOf(
        Car("1", "Toyota Corolla", R.drawable.ic_car1, "John's Dealership", "$15,000"),
        Car("2", "Honda Civic", R.drawable.ic_car2, "FastCars Inc.", "$18,500"),
        Car("3", "Ford Mustang", R.drawable.ic_car3, "Luxury Motors", "$25,000")
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

        // Active Car Display
        activeCar?.let { car ->
            ActiveCarDisplay(car = car)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horizontal Car List
        Text(
            text = "Available Cars for Sale",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cars.size) { index ->
                CarCard(car = cars[index], onClick = { activeCar = cars[index] })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment Buttons
        activeCar?.let { car ->
            PaymentOptions(car = car, navController = navController)
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
            placeholder = { Text("Search for cars...") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { /* Handle search logic here */ }) {
            Text("Search")
        }
    }
}

@Composable
fun ActiveCarDisplay(car: Car) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = car.imageRes),
            contentDescription = car.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = car.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = car.price, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CarCard(car: Car, onClick: () -> Unit) {
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
                painter = painterResource(id = car.imageRes),
                contentDescription = car.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = car.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PaymentOptions(car: Car, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Proceed to sell ${car.name}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = { /* Handle Bank Transfer Logic for Selling Car */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Sell via Bank Transfer")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // Navigate to Transaction Screen
                navController.navigate(ROUTE_TRANSACTIONS)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text("Complete Sale")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SellCarsScreenPreview() {
    SellCarsScreen(navController = rememberNavController())
}
