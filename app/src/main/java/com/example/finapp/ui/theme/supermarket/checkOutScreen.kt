package com.example.finapp.ui.theme.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finapp.ui.theme.cart.CartItem

// Define the CartItem data class
data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

@Composable
fun CheckoutScreen(cart: List<CartItem>, navController: NavController) {
    var totalPrice by remember { mutableStateOf(0.0) }

    // Calculate the total price
    LaunchedEffect(cart) {
        totalPrice = cart.sumOf { it.price * it.quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Checkout Title
        Text(
            text = "Checkout",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display Cart Items
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            cart.forEach { cartItem ->
                // Check if cartItem is not null
                if (cartItem != null) {
                    Text(
                        text = "${cartItem.name} x ${cartItem.quantity} = $${cartItem.price * cartItem.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Price
        Text(
            text = "Total: $$totalPrice",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Proceed to Payment Button
        Button(
            onClick = {
                // Navigate to payment screen
                navController.navigate("payment")
            }
        ) {
            Text("Proceed to Payment")
        }
    }
}
