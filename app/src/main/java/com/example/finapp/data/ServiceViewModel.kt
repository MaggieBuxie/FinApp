import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.ui.buy_airtime.BuyAirtimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServiceState(
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class ServiceViewModel : ViewModel() {
    internal val _buyAirtimeState = MutableStateFlow(ServiceState())
    val buyAirtimeState: StateFlow<ServiceState> get() = _buyAirtimeState

    internal val _payBillsState = MutableStateFlow(ServiceState())
    val payBillsState: StateFlow<ServiceState> get() = _payBillsState

    private val _requestMoneyState = MutableStateFlow(ServiceState())
    val requestMoneyState: StateFlow<ServiceState> get() = _requestMoneyState

    internal fun setSuccessState(stateFlow: MutableStateFlow<ServiceState>, message: String) {
        stateFlow.update { it.copy(successMessage = message, errorMessage = null, isLoading = false) }
    }

    internal fun setErrorState(stateFlow: MutableStateFlow<ServiceState>, error: String) {
        stateFlow.update { it.copy(errorMessage = error, successMessage = null, isLoading = false) }
    }
    // Handle Buy Airtime
    fun buyAirtime(phoneNumber: String, amount: Double) {
        viewModelScope.launch {
            _buyAirtimeState.update { it.copy(isLoading = true) }

            if (!BuyAirtimeUtils.isValidPhoneNumber(phoneNumber)) {
                setErrorState(_buyAirtimeState, "Invalid phone number")
                return@launch
            }

            val formattedPhone = BuyAirtimeUtils.formatPhoneNumber(phoneNumber)
            val fee = BuyAirtimeUtils.calculateTransactionFee(amount)

            setSuccessState(_buyAirtimeState, "Airtime purchase successful for $formattedPhone. Fee: KES $fee")
        }
    }

    // Handle Pay Bills
    fun payBill(billType: String, accountNumber: String, amount: Double) {
        viewModelScope.launch {
            _payBillsState.update { it.copy(isLoading = true) }

            if (amount <= 0) {
                setErrorState(_payBillsState, "Invalid amount")
                return@launch
            }

            setSuccessState(_payBillsState, "Bill payment successful for account $accountNumber")
        }
    }

    // Handle Request Money
    fun requestMoney(name: String, phone: String, amount: Double) {
        viewModelScope.launch {
            _requestMoneyState.update { it.copy(isLoading = true) }

            if (!BuyAirtimeUtils.isValidPhoneNumber(phone)) {
                setErrorState(_requestMoneyState, "Invalid phone number")
                return@launch
            }

            if (amount <= 0) {
                setErrorState(_requestMoneyState, "Invalid amount")
                return@launch
            }

            setSuccessState(_requestMoneyState, "Money request sent to $name ($phone) for KES $amount")
        }
    }
}
