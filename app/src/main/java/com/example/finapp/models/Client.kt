package com.example.finapp.models

data class Client(
    var imageUrl: String = "",         // URL for the client's profile picture
    var firstName: String = "",        // Client's first name
    var lastName: String = "",         // Client's last name
    var gender: String = "",           // Gender of the client
    var age: String = "",              // Age of the client
    var id: String = "",               // Unique ID for the client (e.g., Firebase ID)
    val registrationDate: String = "", // Add registration date
    val kycStatus: Boolean = false, // Add KYC status
    var bio: String = "",              // Short biography or description of the client
    var phoneNumber: String = "",      // Contact phone number of the client
    var email: String = "",            // Email address of the client
    var address: String = "",          // Residential or business address
    var transactions: String = ""      // Transaction details or history (can be expanded as needed)
    )
