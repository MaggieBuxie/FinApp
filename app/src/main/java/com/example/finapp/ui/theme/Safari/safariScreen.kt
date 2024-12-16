package com.example.finapp.ui.theme.Safari


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
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
import com.example.finapp.R
import com.example.finapp.ui.theme.FinAppTheme

// Data model to represent each safari
data class Safari(
    val id: String,
    val name: String,
    val location: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun BookSafariScreen(navController: NavController) {
    // Sample data for safaris (wildlife sceneries)
    var safaris by remember {
        mutableStateOf(
            listOf(
                Safari("1", "African Safari", "Kenya", "$1500", R.drawable.ic_safari1),
                Safari("2", "Savanna Adventure", "South Africa", "$2000", R.drawable.ic_safari2),
                Safari("3", "Jungle Safari", "India", "$1200", R.drawable.ic_safari3),
                Safari("4", "Desert Safari", "Dubai", "$1800", R.drawable.mahali_mzuri2)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Description of the service at the top
        Text(
            text = "Book Your Dream Safari",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Explore wildlife from different parts of the world. Choose your preferred safari and book your adventure!",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display List of Safaris
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(safaris.size) { index ->
                SafariCard(safari = safaris[index], onBook = { safari ->
                    // Handle booking logic here, e.g., navigating to payment
                    navController.navigate("payment_screen")
                })
            }
        }

        // Button to add payment methods
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Navigate to add payment methods screen
            navController.navigate("payment_methods")
        }) {
            Text("Add Payment Methods")
        }
    }
}

@Composable
fun SafariCard(safari: Safari, onBook: (Safari) -> Unit) {
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
                painter = painterResource(id = safari.imageRes),
                contentDescription = safari.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = safari.name, style = MaterialTheme.typography.bodyMedium)
                Text(text = safari.location, style = MaterialTheme.typography.bodySmall)
                Text(text = safari.price, style = MaterialTheme.typography.bodyLarge)
            }

            Button(onClick = { onBook(safari) }) {
                Text("Book Now")
            }
        }
    }
}

@Composable
fun PaymentScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Payment for Safari",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Enter your payment details to complete your booking.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Simulating the payment process (You can replace it with actual payment gateway integration)
        Button(onClick = {
            // Handle payment logic
            navController.navigate("confirmation_screen")
        }) {
            Text("Proceed to Payment")
        }
    }
}

@Composable
fun AddPaymentMethodsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Payment Methods",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Add your payment methods to complete future bookings.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Add Payment Methods form here (you can integrate payment SDK)
        Button(onClick = {
            // Handle adding payment method logic
            navController.navigate("book_safari_screen")
        }) {
            Text("Save Payment Method")
        }
    }
}

// Preview of the screen
@Preview(showBackground = true)
@Composable
fun BookSafariScreenPreview() {
    FinAppTheme() {
        val navController = rememberNavController()
        BookSafariScreen(navController)
    }
}
