package com.example.finapp.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminSettingsViewModel : ViewModel() {

    // StateFlow for Admin Details
    private val _adminDetails = MutableStateFlow(AdminDetails("", "", ""))
    val adminDetails: StateFlow<AdminDetails> get() = _adminDetails

    // StateFlow for loading and error states
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // Firebase Repositories
    private val adminDatabase = FirebaseDatabase.getInstance().getReference("Admin")
    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * Fetch Admin Details from Firebase Database
     */
    fun fetchAdminDetails() {
        _loading.value = true
        val adminId = firebaseAuth.currentUser?.uid
        adminId?.let {
            adminDatabase.child(it).get().addOnSuccessListener { snapshot ->
                val admin = snapshot.getValue(AdminDetails::class.java)
                if (admin != null) {
                    _adminDetails.value = admin
                }
                _loading.value = false
            }.addOnFailureListener { exception ->
                _error.value = "Failed to fetch admin details: ${exception.message}"
                _loading.value = false
            }
        } ?: run {
            _error.value = "No user logged in."
            _loading.value = false
        }
    }

    /**
     * Update Admin Settings in Firebase Database and optionally update Firebase Auth
     */
    fun updateAdminSettings(
        context: Context,
        username: String,
        email: String,
        password: String
    ) {
        _loading.value = true
        val adminId = firebaseAuth.currentUser?.uid
        if (adminId != null) {
            val updatedAdmin = AdminDetails(username, email, password)

            // Update admin details in Firebase Realtime Database
            adminDatabase.child(adminId).setValue(updatedAdmin)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        // Update Firebase Auth email if changed
                        if (email != firebaseAuth.currentUser?.email) {
                            firebaseAuth.currentUser?.updateEmail(email)?.addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    Toast.makeText(context, "Email updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        // Update Firebase Auth password if provided
                        if (password.isNotEmpty()) {
                            firebaseAuth.currentUser?.updatePassword(password)?.addOnCompleteListener { passwordTask ->
                                if (passwordTask.isSuccessful) {
                                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        Toast.makeText(context, "Admin settings updated successfully", Toast.LENGTH_SHORT).show()
                        _loading.value = false
                    } else {
                        _error.value = "Failed to update admin settings"
                        _loading.value = false
                        Toast.makeText(context, "Failed to update admin settings", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            _error.value = "No user logged in."
            _loading.value = false
        }
    }

    /**
     * Data class representing Admin Details
     */
    data class AdminDetails(
        val username: String,
        val email: String,
        val password: String
    )
}
