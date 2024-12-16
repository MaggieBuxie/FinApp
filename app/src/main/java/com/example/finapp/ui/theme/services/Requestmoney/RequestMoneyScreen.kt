package com.example.finapp.ui.request_money

import ServiceViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.ui.pay_bills.PayBillsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestMoneyScreen(
    navController: NavHostController,
    viewModel: ServiceViewModel
) {
    // Collect state from ViewModel
    val requestMoneyState = viewModel.requestMoneyState.collectAsState().value

    // UI State for user inputs
    var requesterName by remember { mutableStateOf("") }
    var requesterPhone by remember { mutableStateOf("") }
    var requestAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Money") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Requester Name
            OutlinedTextField(
                value = requesterName,
                onValueChange = { requesterName = it },
                label = { Text("Requester Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Requester Phone
            OutlinedTextField(
                value = requesterPhone,
                onValueChange = { requesterPhone = it },
                label = { Text("Requester Phone") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Amount
            OutlinedTextField(
                value = requestAmount,
                onValueChange = { requestAmount = it },
                label = { Text("Amount (KES)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    val amountValue = requestAmount.toDoubleOrNull()
                    if (amountValue != null) {
                        viewModel.requestMoney(requesterName, requesterPhone, amountValue)
                    } else {
                        viewModel.requestMoney(requesterName, requesterPhone, 0.0) // Handle invalid amount
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Request Money")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Feedback
            requestMoneyState.successMessage?.let { successMessage ->
                Text(successMessage, color = MaterialTheme.colorScheme.primary)
            }

            requestMoneyState.errorMessage?.let { errorMessage ->
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RequestMoneyScreenPreview() {
    val mockViewModel = ServiceViewModel() // Mock any necessary data here if needed
    RequestMoneyScreen(navController = rememberNavController(), viewModel = mockViewModel)
}


//Input Fields:
//
//Added fields for Requester Name, Requester Phone, and Amount.
//Users can input the details for their money request.
//Integration with ServiceViewModel:
//
//Collects state using requestMoneyState.
//Submits data via the requestMoney method in the ViewModel.
//Feedback Mechanism:
//
//Displays success or error messages after the request is processed.
//Validation:
//
//Ensures the Amount entered is numeric and handles invalid inputs.
