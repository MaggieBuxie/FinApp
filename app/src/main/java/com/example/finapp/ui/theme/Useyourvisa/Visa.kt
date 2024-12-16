package com.example.finapp.ui.theme.Useyourvisa

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

data class CardOption(
    val id: String,
    val name: String,
    val imageRes: Int,
    val description: String
)

@Composable
fun UseYourVisaScreen(navController: NavController) {
    var selectedCard by remember { mutableStateOf<CardOption?>(null) }
    val cardOptions = listOf(
        CardOption("1", "Visa", R.drawable.ic_visa, "Pay securely with your Visa card"),
        CardOption("2", "MasterCard", R.drawable.ic_mastercard, "Pay securely with your MasterCard"),
        CardOption("3", "American Express", R.drawable.ic_amex, "Pay securely with your American Express card")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Search Bar (optional)
        SearchBar()

        Spacer(modifier = Modifier.height(20.dp))

        // Selected Card Display
        selectedCard?.let { card ->
            SelectedCardDisplay(card = card)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horizontal Card List
        Text(
            text = "Select Your Payment Card",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cardOptions.size) { index ->
                CardSelectionCard(card = cardOptions[index], onClick = { selectedCard = cardOptions[index] })
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment Button
        selectedCard?.let { card ->
            PaymentButton(card = card, navController = navController)
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
            placeholder = { Text("Search for payment options...") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { /* Handle search logic here */ }) {
            Text("Search")
        }
    }
}

@Composable
fun SelectedCardDisplay(card: CardOption) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = card.imageRes),
            contentDescription = card.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = card.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = card.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CardSelectionCard(card: CardOption, onClick: () -> Unit) {
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
                painter = painterResource(id = card.imageRes),
                contentDescription = card.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = card.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PaymentButton(card: CardOption, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Proceed to pay with ${card.name}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                // Navigate to Transaction Screen
                navController.navigate(ROUTE_TRANSACTIONS)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text("Pay Now")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UseYourVisaScreenPreview() {
    UseYourVisaScreen(navController = rememberNavController())
}
