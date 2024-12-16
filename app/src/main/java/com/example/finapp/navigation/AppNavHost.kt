package com.example.finapp.navigation

import AdminHomeScreen
import AdminSettingsScreen
import PaymentScreen
import ServiceScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finapp.main.SplashScreen
import com.example.finapp.ui.grow.GrowthScreen
import com.example.finapp.ui.pay_bills.PayBillsScreen
import com.example.finapp.ui.request_money.RequestMoneyScreen
import com.example.finapp.ui.theme.BuyAirtimeScreen
import com.example.finapp.ui.theme.Groceries.GroceryShoppingScreen
import com.example.finapp.ui.theme.ProfileScreen
import com.example.finapp.ui.theme.ProfileScreen.EditProfileScreen
import com.example.finapp.ui.theme.Safari.BookSafariScreen
import com.example.finapp.ui.theme.buyCarsScreen.BuyCarsScreen
import com.example.finapp.ui.theme.cart.CartItem
import com.example.finapp.ui.theme.cart.CartScreen
import com.example.finapp.ui.theme.checkout.CheckoutScreen
import com.example.finapp.ui.theme.exploreServiceScreen.ExploreServicesScreen
import com.example.finapp.ui.theme.home.HomeScreen
import com.example.finapp.ui.theme.home.ROUTE_UPDATE_ADMIN_SETTINGS
import com.example.finapp.ui.theme.login.LoginScreen
import com.example.finapp.ui.theme.registerScreen.RegisterScreen
import com.example.finapp.ui.theme.screens.clients.AddClient
import com.example.finapp.ui.theme.screens.clients.UpdateClient
import com.example.finapp.ui.theme.screens.client.ViewClients
import com.example.finapp.ui.theme.sellCars.SellCarsScreen
import com.example.finapp.ui.theme.signup.SignupScreen
import com.example.finapp.ui.theme.supermarket.SupermarketScreen
import com.example.finapp.ui.theme.transactions.TransactionScreen
import com.example.realestateapp.main.RealEstateScreen
import com.example.realestateapp.main.ViewHouseScreen
import com.example.uberapp.GetUberScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_LOGIN
) {
    val serviceViewModel: ServiceViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {

        // Authentication Screens
        composable(ROUTE_SPLASH_ACTIVITY) { SplashScreen(navController) }
        composable(ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(ROUTE_SIGNUP) { SignupScreen(navController) }
        composable(ROUTE_LOGIN) { LoginScreen(navController) }

        // Home and Profile
        composable(ROUTE_HOME) { HomeScreen(navController) }
        composable(ROUTE_PROFILE) { ProfileScreen(navController) }
        // Admin Screens
        composable(ROUTE_ADMIN_HOME) { AdminHomeScreen(navController) }
        composable(ROUTE_ADMIN_SETTINGS) { AdminSettingsScreen(navController) }
        composable(ROUTE_EDIT_PROFILE) { EditProfileScreen(navController) }
        // Client Management
        composable(ROUTE_ADD_CLIENT) { AddClient(navController) }
        composable(ROUTE_VIEW_CLIENTS) { ViewClients(navController) }
        composable(ROUTE_UPDATE_CLIENT) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            UpdateClient(navController)
        }
        composable("real_estate_screen") {
            RealEstateScreen(navController, isAdmin = false)
        }
        composable("view_house/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            ViewHouseScreen(houseId = houseId, navController = navController)
        }
        composable("payment_screen/{houseId}") { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            PaymentScreen(navController = navController)
        }
        composable(ROUTE_UPDATE_ADMIN_SETTINGS) { AdminSettingsScreen(navController) }


        // Service Screens
        composable(ROUTE_SERVICES) { ServiceScreen(navController) }
        composable(ROUTE_BUY_AIRTIME) { BuyAirtimeScreen(navController, serviceViewModel) }
        composable(ROUTE_PAY_BILLS) { PayBillsScreen(navController, serviceViewModel) }
        composable(ROUTE_REQUEST_MONEY) { RequestMoneyScreen(navController, serviceViewModel) }

        // Shopping and Checkout
        composable(ROUTE_SUPERMARKET) { SupermarketScreen(navController) }
        composable(ROUTE_GROCERIES) { GroceryShoppingScreen(navController) }
        composable(ROUTE_CART_ITEM) { backStackEntry ->
            val cartItems = backStackEntry.arguments?.getParcelableArrayList<CartItem>("cartItems") ?: emptyList()
            CartScreen(
                cart = cartItems,
                onRemoveItem = { cartItem ->
                    // Logic for removing an item from the cart
                },
                navController = navController // Passing NavController directly
            )
        }

        composable(ROUTE_CHECKOUT) { backStackEntry ->
            val cartItems = backStackEntry.arguments?.getParcelableArrayList<CartItem>("cartItems") ?: emptyList()
            CheckoutScreen(cartItems, navController)
        }
        composable(ROUTE_PAY_BILLS) { PayBillsScreen(navController) }
        composable(ROUTE_EXPLORE_SERVICES) { ExploreServicesScreen(navController) }
        // Travel and Real Estate
        composable(ROUTE_BOOK_SAFARI) { BookSafariScreen(navController) }
        composable(ROUTE_REAL_ESTATE) { RealEstateScreen(navController, isAdmin = true) }
        composable(ROUTE_UBER) { GetUberScreen(navController) }
        composable(ROUTE_GROWTH) {GrowthScreen(navController)}

        // Car Buying and Selling
        composable(ROUTE_BUY_CARS) { BuyCarsScreen(navController) }
        composable(ROUTE_SELL_CARS) { SellCarsScreen(navController) }
        
        // Transactions and Payments
        composable(ROUTE_TRANSACTIONS) {
            val context = LocalContext.current
            TransactionScreen(navController = navController, context = context)
        }
        composable(ROUTE_PAY) { PaymentScreen(navController) }
    }
}
