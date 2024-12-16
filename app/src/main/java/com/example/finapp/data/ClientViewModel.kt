package com.example.finapp.data

import android.R.attr.id
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.finapp.models.Client
import com.example.finapp.navigation.ROUTE_VIEW_CLIENTS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener


class ClientViewModel(): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =_isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> get() = _successMessage

    fun saveClient(firstname:String, lastname:String,
                   gender:String, age:String, navController: NavController, context:Context){
        val id = System.currentTimeMillis().toString()
        val dbRef = FirebaseDatabase.getInstance().getReference("Client/$id")

        val clientData = Client("", firstname, lastname, gender, age, id)
        dbRef.setValue(clientData).addOnCompleteListener{ task ->
            if (task.isSuccessful){
                showToast("Client added successfully", context)
                navController.navigate(ROUTE_VIEW_CLIENTS)
            }else{
                showToast("Client not added successfully", context)
            }
        }
    }

    fun viewClients(client: MutableState<Client>,
                    clients: SnapshotStateList<Client>,
                    context: Context): SnapshotStateList<Client>{
        val ref = FirebaseDatabase.getInstance().getReference().child("Client")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                clients.clear()
                for (snap in snapshot.children){
                    val value = snap.getValue(Client::class.java)
                    client.value = value!!
                    clients.add(value)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to fetch clients", context)
            }
        })
        return clients
    }


    fun updateClient(
        context: Context, navController: NavController,
        firstname: String, lastname: String, gender: String, age: String, id: String, currentImageUrl: String, bio: String, filePath: Uri
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Client/$id")
        val updatedClient = Client("", firstname, lastname, gender, age, id)

        databaseReference.setValue(updatedClient).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Client updated successfully", context)
                navController.navigate(ROUTE_VIEW_CLIENTS) // Ensure ROUTE_VIEW_CLIENT is defined
            } else {
                showToast("Record update failed: ${task.exception?.message}", context) // Log the error message
            }
        }
    }

    fun deleteClient(context: Context, id: String, navController: NavController) {
        AlertDialog.Builder(context)
            .setTitle("Delete the Client")
            .setMessage("Are you sure you want to delete this Client?")
            .setPositiveButton("Yes") { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().getReference("Client/$id")
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Client deleted successfully", context)
                        navController.navigate(ROUTE_VIEW_CLIENTS) // Navigate back or refresh the UI
                    } else {
                        showToast("Client has not been deleted: ${task.exception?.message}", context) // Log the error message
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun Transaction(context: Context, transactions: List<Transaction>, transaction: Transaction, navController: NavController, clientId: String,id: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Client/$clientId/transactions")
        val transactionId = databaseReference.push().key// Generate a unique ID for the new transaction

        AlertDialog.Builder(context)
            .setTitle("Send")
            .setMessage("Are you sure you want to proceed with this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                // Assuming you want to remove the transaction from the client's record
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Transaction was successful", context)
                    } else {
                        showToast("Transaction failed: ${task.exception?.message}", context) // Log the error message
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    public fun showToast(message:String,context: Context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()}
}

