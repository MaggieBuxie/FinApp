package com.example.finapp.ui.pay_bills

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.example.finapp.ui.theme.FinAppTheme
import androidx.navigation.compose.rememberNavController
import com.example.finapp.data.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillsScreen(
    navController: NavHostController,
    serviceViewModel: ServiceViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    var accountNumber by remember { mutableStateOf("") }
    var billAmount by remember { mutableStateOf("") }
    var billType by remember { mutableStateOf("") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val context = LocalContext.current
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        // Fetch the user's phone number when the screen loads
        authViewModel.getUserPhoneNumber(userId) { retrievedPhoneNumber ->
            phoneNumber = retrievedPhoneNumber
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pay Bills") },
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
            // Input Fields
            OutlinedTextField(
                value = billType,
                onValueChange = { billType = it },
                label = { Text("Bill Type (e.g., Electricity)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = billAmount,
                onValueChange = { billAmount = it },
                label = { Text("Amount (KES)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                // Pay Button
                Button(
                    onClick = { showConfirmationDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Pay")
                }
            }

            // Confirmation Dialog
            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Confirm Payment") },
                    text = {
                        Text("Are you sure you want to pay KES $billAmount for $billType?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showConfirmationDialog = false
                                val amountValue = billAmount.toDoubleOrNull()
                                if (amountValue != null && phoneNumber.isNotBlank()) {
                                    isLoading = true
                                    saveBillPaymentToFirebase(
                                        context,
                                        billType,
                                        accountNumber,
                                        amountValue,
                                        phoneNumber
                                    ) {
                                        isLoading = false
                                        // Navigate to the Transaction Screen
                                        navController.navigate("transaction_screen")
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please enter valid details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmationDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("route_home") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Go Back to Menu")
            }
        }
    }
}

/**
 * Save bill payment details to Firebase Realtime Database.
 */
fun saveBillPaymentToFirebase(
    context: android.content.Context,
    billType: String,
    accountNumber: String,
    amount: Double,
    phoneNumber: String,
    onComplete: () -> Unit
) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Transactions")
    val transactionId = databaseReference.push().key
    val transaction = mapOf(
        "service" to billType,
        "amount" to amount,
        "phone" to phoneNumber,
        "accountNumber" to accountNumber
    )

    transactionId?.let {
        databaseReference.child(it).setValue(transaction)
            .addOnSuccessListener {
                Toast.makeText(context, "Payment saved successfully", Toast.LENGTH_SHORT).show()
                onComplete()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save payment: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                onComplete()
            }
    } ?: run {
        Toast.makeText(context, "Failed to generate transaction ID", Toast.LENGTH_SHORT).show()
        onComplete()
    }
}
