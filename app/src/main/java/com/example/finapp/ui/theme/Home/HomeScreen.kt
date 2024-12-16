package com.example.finapp.ui.theme.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finapp.data.AuthViewModel
import com.example.finapp.navigation.ROUTE_EXPLORE_SERVICES
import com.example.finapp.navigation.ROUTE_GROWTH
import com.example.finapp.navigation.ROUTE_PAY
import com.example.finapp.navigation.ROUTE_PROFILE
import com.example.finapp.navigation.ROUTE_SERVICES
import com.example.finapp.navigation.ROUTE_TRANSACTIONS
import com.example.finapp.ui.theme.exploreServiceScreen.ExploreServicesScreen

// New route for Update Admin Settings
const val ROUTE_UPDATE_ADMIN_SETTINGS = "update_admin_settings"

@Composable
fun HomeScreen(navController: NavController) {
    // ViewModel for dynamic data
    val authViewModel: AuthViewModel = viewModel()

    // Get current route to manage the selected item in the BottomNavigationBar
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val userName = ""
            WelcomeMessage(userName = userName)
            Spacer(modifier = Modifier.height(20.dp))
            val walletBalance = ""
            WalletBalance(balance = walletBalance)
            Spacer(modifier = Modifier.height(10.dp))
            SearchBar() // Search bar below wallet balance
            Spacer(modifier = Modifier.height(10.dp))
            PayBillsButton(navController = navController)
            Spacer(modifier = Modifier.height(10.dp))
            ViewTransactionsButton(navController = navController)
            Spacer(modifier = Modifier.height(20.dp))
            UpdateAdminSettingsButton(navController = navController) // Update Settings Button
            Spacer(modifier = Modifier.height(20.dp)) // Add spacing between buttons
            PayButton(navController = navController) // New PAY button
            Spacer(modifier = Modifier.height(20.dp)) // Add spacing between buttons
            ExploreServicesScreenButton(navController = navController)

        }
    }
}

// Search Bar for Services
@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf("") }

    // TextField for entering the search query
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it }, // Update the search query as user types
            label = { Text("Search for Services") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            }
        )
    }
}


// New Button for navigating to Update Admin Settings
@Composable
fun UpdateAdminSettingsButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_UPDATE_ADMIN_SETTINGS) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Update Admin Settings")
    }
}

// PAY button below Update Settings
@Composable
fun PayButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_PAY) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("PAY")
    }
}


// Services
@Composable
fun ExploreServicesScreenButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_EXPLORE_SERVICES) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Explore our Services")
    }
}



@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavigationItem(
                selected = currentRoute == "home",
                onClick = { navController.navigate("home") },
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )
            BottomNavigationItem(
                selected = currentRoute == ROUTE_TRANSACTIONS,
                onClick = { navController.navigate(ROUTE_TRANSACTIONS) },
                icon = { Icon(Icons.Filled.List, contentDescription = "Transactions") },
                label = { Text("Transactions") }
            )
            BottomNavigationItem(
                selected = currentRoute == ROUTE_PROFILE,
                onClick = { navController.navigate(ROUTE_PROFILE) },
                icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                label = { Text("Profile") }
            )
            BottomNavigationItem(
                selected = currentRoute == ROUTE_GROWTH,
                onClick = { navController.navigate(ROUTE_GROWTH) },
                icon = { Icon(Icons.Filled.Person, contentDescription = "Growth") },
                label = { Text("Growth") }
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        label()
    }
}

@Composable
fun WelcomeMessage(userName: String) {
    Text(
        text = "Welcome, $userName!",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun WalletBalance(balance: String) {
    Text(
        text = "Wallet Balance: $balance",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun PayBillsButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_SERVICES) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Pay Bills")
    }
}

@Composable
fun ViewTransactionsButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_TRANSACTIONS) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("View Transactions")
    }
}

@Composable
fun PaymentScreenButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(ROUTE_PAY) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("PAY HERE")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}
