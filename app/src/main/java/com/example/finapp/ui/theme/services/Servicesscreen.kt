import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import com.example.finapp.ui.theme.FinAppTheme

class ServiceScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ServiceScreen(navController = rememberNavController())
                }
            }
        }
    }
}

@Composable
fun ServiceScreen(navController: NavController, serviceViewModel: ServiceViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Buy Airtime", "Pay Bills", "Request Money")

    Column {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> BuyAirtimeSection(serviceViewModel, navController)
            1 -> PayBillsSection(serviceViewModel, navController)
            2 -> RequestMoneySection(serviceViewModel, navController)
        }
    }
}

@Composable
fun BuyAirtimeSection(serviceViewModel: ServiceViewModel, navController: NavController) {
    val state by serviceViewModel.buyAirtimeState.collectAsState()
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }

    ServiceInputSection(
        state = state,
        onAction = {
            serviceViewModel.buyAirtime(phoneNumber.text, amount.text.toDoubleOrNull() ?: 0.0)
            saveTransactionToDatabase("Airtime", amount.text.toDoubleOrNull() ?: 0.0, phoneNumber.text)
            navController.navigate("transaction_screen")
        },
        inputs = {
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun PayBillsSection(serviceViewModel: ServiceViewModel, navController: NavController) {
    val state by serviceViewModel.payBillsState.collectAsState()
    var billType by remember { mutableStateOf(TextFieldValue("")) }
    var accountNumber by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }

    ServiceInputSection(
        state = state,
        onAction = {
            serviceViewModel.payBill(billType.text, accountNumber.text, amount.text.toDoubleOrNull() ?: 0.0)
            saveTransactionToDatabase(billType.text, amount.text.toDoubleOrNull() ?: 0.0, accountNumber.text)
            navController.navigate("transaction_screen")
        },
        inputs = {
            TextField(
                value = billType,
                onValueChange = { billType = it },
                label = { Text("Bill Type") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun RequestMoneySection(serviceViewModel: ServiceViewModel, navController: NavController) {
    val state by serviceViewModel.requestMoneyState.collectAsState()
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf(TextFieldValue("")) }

    ServiceInputSection(
        state = state,
        onAction = {
            serviceViewModel.requestMoney(name.text, phone.text, amount.text.toDoubleOrNull() ?: 0.0)
            saveTransactionToDatabase("Money Request", amount.text.toDoubleOrNull() ?: 0.0, phone.text)
            navController.navigate("transaction_screen")
        },
        inputs = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun ServiceInputSection(
    state: ServiceState,
    onAction: () -> Unit,
    inputs: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        inputs()

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onAction() }, modifier = Modifier.fillMaxWidth()) {
            Text("Pay")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            state.successMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary)
            }

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// Function to save transaction details to Firebase
fun saveTransactionToDatabase(service: String, amount: Double, phone: String) {
    val transaction = Transaction(service, amount, phone)
    val transactionDatabase = FirebaseDatabase.getInstance().getReference("Transactions")
    val transactionId = transactionDatabase.push().key ?: return

    transactionDatabase.child(transactionId).setValue(transaction).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Transaction saved successfully
        } else {
            // Error saving transaction
        }
    }
}

data class Transaction(
    val service: String,
    val amount: Double,
    val phone: String
)

@Preview(showBackground = true)
@Composable
fun ServiceScreenPreview() {
    FinAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            ServiceScreen(navController = rememberNavController())
        }
    }
}
