package com.example.finapp.ui.theme.transactions

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.finapp.navigation.ROUTE_HOME
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.util.*

// Data class to represent a transaction
data class Transaction(
    val service: String = "",
    val amount: Double = 0.0,
    val phone: String = ""
)

// Transaction Screen
@Composable
fun TransactionScreen(navController: NavController, context: Context) {
    var isDownloading by remember { mutableStateOf(false) }
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Fetch transactions when the screen is loaded
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            transactions = fetchTransactionsFromFirebase()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title
        Text(
            text = "Transaction History",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Error message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }

        // Transaction list or empty state
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            if (transactions.isEmpty()) {
                Text(
                    text = "No transactions available.",
                    style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionRow(transaction)
                    }
                }
            }
        }

        // Action buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(ROUTE_HOME) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Back to Main Menu", color = Color.White)
            }

            Button(
                onClick = {
                    isDownloading = true
                    downloadStatement(context, transactions) {
                        isDownloading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(text = "Download Statement", color = Color.White)
                }
            }
        }
    }
}

// Row for each transaction in the list
@Composable
fun TransactionRow(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Service: ${transaction.service}", style = TextStyle(fontSize = 16.sp))
            Text("Amount: $${transaction.amount}", style = TextStyle(fontSize = 16.sp))
            Text("Phone: ${transaction.phone}", style = TextStyle(fontSize = 16.sp))
        }
    }
}

// Fetch transactions from Firebase Realtime Database
suspend fun fetchTransactionsFromFirebase(): List<Transaction> {
    val databaseReference = FirebaseDatabase.getInstance().getReference("Transactions")
    return try {
        val dataSnapshot = databaseReference.get().await()
        dataSnapshot.children.mapNotNull { it.getValue<Transaction>() }
    } catch (e: Exception) {
        emptyList() // Return an empty list in case of an error
    }
}

// Downloads the transaction statement to the device
fun downloadStatement(context: Context, transactions: List<Transaction>, onComplete: () -> Unit) {
    try {
        // Generate statement content
        val statementContent = buildString {
            append("Transaction History:\n")
            append("----------------------------\n")
            transactions.forEachIndexed { index, transaction ->
                append("${index + 1}. ${transaction.service}: $${transaction.amount} - Phone: ${transaction.phone}\n")
            }
        }

        // Define file name and location
        val fileName = "transaction_statement.txt"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        // Write content to the file
        FileOutputStream(file).use { it.write(statementContent.toByteArray()) }

        Toast.makeText(context, "Statement downloaded to ${file.absolutePath}", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to download statement: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        onComplete()
    }
}

// Preview for the Transaction Screen
@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    val navController = rememberNavController()
    TransactionScreen(navController, context = null as Context) // Provide a valid context in real usage
}
