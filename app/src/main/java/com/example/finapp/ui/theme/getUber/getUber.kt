package com.example.uberapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.ui.theme.FinAppTheme
import com.example.finapp.ui.theme.FinAppTheme
import com.example.finapp.R

data class RideOption(
    val id: String,
    val type: String,
    val price: String,
    val estimatedTime: String,
    val imageRes: Int
)

data class Driver(
    val name: String,
    val carModel: String,
    val carColor: String,
    val rating: Float,
    val profileImageRes: Int
)

@Composable
fun GetUberScreen(navController: NavController) {
    var currentLocation by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }

    // Sample list of ride options
    val rideOptions = listOf(
        RideOption("1", "Uber1", "$12.50", "5 mins", R.drawable.ic_uber1),
        RideOption("2", "Uber2", "$18.00", "7 mins", R.drawable.ic_uber2),
        RideOption("3", "Uber 3", "$30.00", "4 mins", R.drawable.ic_uber3)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Get an Uber",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Pickup Location
        Text("Current Location:")
        BasicTextField(
            value = currentLocation,
            onValueChange = { currentLocation = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp).background(MaterialTheme.colorScheme.surface),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Destination Location
        Text("Where to?")
        BasicTextField(
            value = destination,
            onValueChange = { destination = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp).background(MaterialTheme.colorScheme.surface),
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Available Ride Options
        Text(
            text = "Available Rides",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(rideOptions.size) { index ->
                RideOptionCard(rideOption = rideOptions[index], onBookClick = { rideOption ->
                    // Navigate to the Driver Details screen
                    navController.navigate("driver_details/${rideOption.id}")
                })
            }
        }
    }
}

@Composable
fun RideOptionCard(rideOption: RideOption, onBookClick: (RideOption) -> Unit) {
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
            // Ride Type Image
            Image(
                painter = painterResource(id = rideOption.imageRes),
                contentDescription = rideOption.type,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Ride Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = rideOption.type, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Price: ${rideOption.price}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Estimated Time: ${rideOption.estimatedTime}", style = MaterialTheme.typography.bodySmall)
            }

            // Book Button
            Button(onClick = { onBookClick(rideOption) }) {
                Text("Book Now")
            }
        }
    }
}

@Composable
fun DriverDetailsScreen(rideOptionId: String, navController: NavController) {
    // Example of a driver with mock data
    val driver = Driver(
        name = "John Doe",
        carModel = "Toyota Prius",
        carColor = "Black",
        rating = 4.9f,
        profileImageRes = R.drawable.ic_driver_profile
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Driver Profile
        Text(
            text = "Driver Details",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Driver Profile Image
        Image(
            painter = painterResource(id = driver.profileImageRes),
            contentDescription = "Driver Profile",
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Driver Information
        Text("Name: ${driver.name}", style = MaterialTheme.typography.bodyMedium)
        Text("Car: ${driver.carModel} - ${driver.carColor}", style = MaterialTheme.typography.bodyMedium)
        Text("Rating: ${driver.rating} ★", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons to proceed
        Button(onClick = {
            // Proceed with the booking or start the ride
        }) {
            Text("Start Ride")
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Back to Ride Options")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GetUberScreenPreview() {
    FinAppTheme() {
        GetUberScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DriverDetailsScreenPreview() {
    FinAppTheme  {
        DriverDetailsScreen(rideOptionId = "1", navController = rememberNavController())
    }
}
