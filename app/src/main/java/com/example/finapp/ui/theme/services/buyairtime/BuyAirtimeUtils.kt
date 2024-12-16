package com.example.finapp.ui.buy_airtime

object BuyAirtimeUtils {

    /**
     * Validates the phone number.
     * @param phoneNumber The phone number to validate.
     * @return True if the phone number is valid, false otherwise.
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Simple regex for a valid phone number
        val regex = Regex("^\\+?\\d{10,13}\$")
        return phoneNumber.matches(regex)
    }

    /**
     * Formats the phone number for consistent processing.
     * @param phoneNumber The phone number to format.
     * @return The formatted phone number.
     */
    fun formatPhoneNumber(phoneNumber: String): String {
        return if (phoneNumber.startsWith("+")) {
            phoneNumber
        } else {
            "+$phoneNumber"
        }
    }

    /**
     * Calculates the transaction fee based on the amount.
     * @param amount The amount for which to calculate the fee.
     * @return The calculated transaction fee.
     */
    fun calculateTransactionFee(amount: Double): Double {
        // Example: Flat rate of 1% of the transaction amount
        val feePercentage = 0.01
        return (amount * feePercentage).coerceAtLeast(1.0) // Minimum fee of 1 KES
    }

    /**
     * Formats the amount to two decimal places.
     * @param amount The amount to format.
     * @return The formatted amount as a string.
     */
    fun formatAmount(amount: Double): String {
        return "%.2f".format(amount)
    }
}
