package com.example.finapp.ui.theme.cart

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Making CartItem a Parcelable so it can be passed as a navigation argument
@SuppressLint("ParcelCreator")
data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val product: String  // You can use an actual product object here, if needed.
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeDouble(price)
        dest.writeInt(quantity)
        dest.writeString(product)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<CartItem> {
            override fun createFromParcel(parcel: Parcel): CartItem {
                return CartItem(
                    id = parcel.readString() ?: "",
                    name = parcel.readString() ?: "",
                    price = parcel.readDouble(),
                    quantity = parcel.readInt(),
                    product = parcel.readString() ?: ""
                )
            }

            override fun newArray(size: Int): Array<CartItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}

@Composable
fun CartScreen(cart: List<CartItem>, onRemoveItem: (CartItem) -> Unit, navController: NavController) {
    var totalPrice by remember { mutableStateOf(0.0) }

    // Calculate the total price whenever the cart is updated
    LaunchedEffect(cart) {
        totalPrice = cart.sumOf { it.price * it.quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Cart",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (cart.isEmpty()) {
            Text(
                text = "No items in your cart.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Column {
                cart.forEach { cartItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${cartItem.name} x ${cartItem.quantity} = $${cartItem.price * cartItem.quantity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = { onRemoveItem(cartItem) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove item")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total: $$totalPrice",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Proceed to Checkout button
            Button(
                onClick = { navController.navigate("checkout") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to Checkout")
            }
        }
    }
}
