package com.example.finapp.ui.theme.ProfileScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finapp.navigation.ROUTE_HOME
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext

// ProfileData.kt
data class ProfileData(
    val name: String = "",
    val email: String = "",
    val phone: String = ""
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController?) {
    // Firebase Authentication instance
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // State variables to hold the editable values
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    var phone by remember { mutableStateOf("+254-700-000-000") }

    // Get the current context (needed for Toast)
    val context = LocalContext.current

    // Fetch the current user data from Firebase if the user is logged in
    LaunchedEffect(Unit) {
        currentUser?.uid?.let { userId ->
            // Fetch the user profile data from Firebase Realtime Database
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

            databaseReference.get().addOnSuccessListener { snapshot ->
                val profile = snapshot.getValue(ProfileData::class.java)
                profile?.let {
                    name = it.name
                    email = it.email
                    phone = it.phone
                }
            }.addOnFailureListener {
                // Handle failure (e.g., user data not found)
                Toast.makeText(context, "Failed to fetch user data", Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Main Menu")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Name Input Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Input Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Changes Button
            Button(
                onClick = {
                    if (currentUser != null) {
                        val userProfile = ProfileData(name, email, phone)
                        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
                            .child(currentUser.uid)

                        // Update the user's profile in Firebase
                        databaseReference.setValue(userProfile).addOnSuccessListener {
                            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            navController?.navigateUp()
                        }.addOnFailureListener { exception ->
                            Toast.makeText(context, "Failed to update profile: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen(navController = null)
}
