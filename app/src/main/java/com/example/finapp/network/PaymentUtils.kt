package com.example.finapp.network
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

fun makePayment(
    context: Context,
    phoneNumber: String,
    amount: String
) {
    val paymentRequest = PaymentRequest(phoneNumber, amount)

    CoroutineScope(Dispatchers.Main).launch {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.makePayment(paymentRequest)
            }

            if (response.isSuccessful) {
                Toast.makeText(context, response.body()?.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Payment failed", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}