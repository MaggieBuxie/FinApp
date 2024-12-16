package com.example.finapp.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.finapp.navigation.ROUTE_ADMIN_HOME
import com.example.finapp.navigation.ROUTE_LOGIN_SCREEN
import com.example.finapp.navigation.ROUTE_SPLASH_ACTIVITY
import com.example.finapp.ui.theme.FinAppTheme
import androidx.compose.ui.res.painterResource

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FinAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SplashScreen(rememberNavController())
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    // Simulate loading or checking user login state
    LaunchedEffect(Unit) {
        delay(2000) // Wait for 3 seconds (or adjust as necessary)

        // Here, decide if the user is logged in or an admin
        // For example, replace this with your actual login check or admin status check
        val isUserLoggedIn = true // This is a placeholder for actual logic
        val isAdmin = false // This is a placeholder for actual logic

        if (isUserLoggedIn) {
            if (isAdmin) {
                navController.navigate(ROUTE_ADMIN_HOME) {
                    // Clear back stack to avoid returning to splash screen
                    popUpTo(ROUTE_SPLASH_ACTIVITY) { inclusive = true }
                }
            } else {
                navController.navigate(ROUTE_LOGIN_SCREEN) {
                    // Clear back stack to avoid returning to splash screen
                    popUpTo(ROUTE_SPLASH_ACTIVITY) { inclusive = true }
                }
            }
        } else {
            navController.navigate(ROUTE_LOGIN_SCREEN) {
                popUpTo(ROUTE_SPLASH_ACTIVITY) { inclusive = true }
            }
        }
    }

    // Splash UI: A welcome message with the app name
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = com.example.finapp.R.drawable.logo3), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier.fillMaxSize() // Ensures image takes up full screen
        )

        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxSize() // Ensures the column also fills the screen
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Vertically centers the content
        ) {
            Text(
                text = "Welcome to FinApp!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Are you ready to take control of your finances?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FinAppTheme {
        SplashScreen(navController = rememberNavController()) // Correct usage
    }
}
