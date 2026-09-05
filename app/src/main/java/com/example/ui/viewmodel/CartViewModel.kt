package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.JorezRepository
import com.example.model.CartItem
import com.example.model.Order
import com.example.model.PaymentMethod
import com.example.model.Product
import com.example.model.ProductVariant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartItemUiModel(
    val cartItem: CartItem,
    val isSelected: Boolean
) {
    val id: String get() = cartItem.id
    val product: Product get() = cartItem.product
    val selectedVariant: ProductVariant get() = cartItem.selectedVariant
    val quantity: Int get() = cartItem.quantity
    val totalPriceUgx: Long get() = cartItem.totalPriceUgx
    val maxStock: Int get() = cartItem.selectedVariant.stock
    val isMaxStockReached: Boolean get() = quantity >= maxStock
}

data class CartUiState(
    val items: List<CartItemUiModel> = emptyList(),
    val selectedItemIds: Set<String> = emptySet(),
    val isAllSelected: Boolean = false,
    val selectedItemsCount: Int = 0,
    val selectedUnitsCount: Int = 0,
    val totalCartUnitsCount: Int = 0,
    val subtotalUgx: Long = 0L,
    val deliveryFeeUgx: Long = 5000L,
    val grandTotalUgx: Long = 0L,
    val isCheckingOut: Boolean = false,
    val isProcessingPayment: Boolean = false,
    val placedOrder: Order? = null,
    val validationMessage: String? = null,
    // Checkout form fields
    val fullName: String = "Julius Maseruka",
    val phone: String = "+256 772 345678",
    val altPhone: String = "+256 701 987654",
    val district: String = "Kampala",
    val cityTown: String = "Ntinda / Nakawa",
    val address: String = "Plot 12 Kimera Road",
    val deliveryInstructions: String = "Call when near the taxi stage.",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.MTN_MOMO
) {
    val hasItems: Boolean get() = items.isNotEmpty()
    val hasSelectedItems: Boolean get() = selectedItemsCount > 0
    val unselectedItemsCount: Int get() = items.size - selectedItemsCount
    val selectedCartItems: List<CartItem> get() = items.filter { it.isSelected }.map { it.cartItem }
}

class CartViewModel(
    private val repository: JorezRepository = JorezRepository.instance
) : ViewModel() {

    private val _selectedItemIds = MutableStateFlow<Set<String>>(emptySet())
    private val _isCheckingOut = MutableStateFlow(false)
    private val _isProcessingPayment = MutableStateFlow(false)
    private val _placedOrder = MutableStateFlow<Order?>(null)
    private val _validationMessage = MutableStateFlow<String?>(null)

    private val _fullName = MutableStateFlow("Julius Maseruka")
    private val _phone = MutableStateFlow("+256 772 345678")
    private val _altPhone = MutableStateFlow("+256 701 987654")
    private val _district = MutableStateFlow("Kampala")
    private val _cityTown = MutableStateFlow("Ntinda / Nakawa")
    private val _address = MutableStateFlow("Plot 12 Kimera Road")
    private val _deliveryInstructions = MutableStateFlow("Call when near the taxi stage.")
    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.MTN_MOMO)

    init {
        _selectedItemIds.value = repository.cart.value.map { it.id }.toSet()
        viewModelScope.launch {
            repository.cart.collect { cartList ->
                val validCurrentIds = cartList.map { it.id }.toSet()
                _selectedItemIds.update { current -> current.intersect(validCurrentIds) }
            }
        }
    }

    private val checkoutFormData = combine(
        combine(_fullName, _phone, _altPhone, _district) { name, phone, alt, dist ->
            listOf(name, phone, alt, dist)
        },
        combine(_cityTown, _address, _deliveryInstructions, _selectedPaymentMethod) { city, addr, inst, method ->
            Pair(listOf(city, addr, inst), method)
        }
    ) { group1, (group2, method) ->
        CheckoutFormState(
            fullName = group1[0],
            phone = group1[1],
            altPhone = group1[2],
            district = group1[3],
            cityTown = group2[0],
            address = group2[1],
            deliveryInstructions = group2[2],
            paymentMethod = method
        )
    }

    private val checkoutStatusData = combine(
        _isCheckingOut, _isProcessingPayment, _placedOrder, _validationMessage
    ) { checkingOut, processing, placed, msg ->
        CheckoutStatus(checkingOut, processing, placed, msg)
    }

    val uiState: StateFlow<CartUiState> = combine(
        repository.cart,
        _selectedItemIds,
        checkoutFormData,
        checkoutStatusData
    ) { cartList, selectedIds, form, status ->
        val uiItems = cartList.map { item ->
            CartItemUiModel(
                cartItem = item,
                isSelected = item.id in selectedIds
            )
        }
        val selectedUiItems = uiItems.filter { it.isSelected }
        val selectedUnits = selectedUiItems.sumOf { it.quantity }
        val totalUnits = cartList.sumOf { it.quantity }
        val subtotal = selectedUiItems.sumOf { it.totalPriceUgx }
        val deliveryFee = if (form.district.equals("Kampala", ignoreCase = true)) 5000L else 12000L
        val grandTotal = if (selectedUiItems.isEmpty()) 0L else (subtotal + deliveryFee)
        val allSelected = cartList.isNotEmpty() && selectedUiItems.size == cartList.size

        CartUiState(
            items = uiItems,
            selectedItemIds = selectedIds,
            isAllSelected = allSelected,
            selectedItemsCount = selectedUiItems.size,
            selectedUnitsCount = selectedUnits,
            totalCartUnitsCount = totalUnits,
            subtotalUgx = subtotal,
            deliveryFeeUgx = deliveryFee,
            grandTotalUgx = grandTotal,
            isCheckingOut = status.isCheckingOut,
            isProcessingPayment = status.isProcessingPayment,
            placedOrder = status.placedOrder,
            validationMessage = status.validationMessage,
            fullName = form.fullName,
            phone = form.phone,
            altPhone = form.altPhone,
            district = form.district,
            cityTown = form.cityTown,
            address = form.address,
            deliveryInstructions = form.deliveryInstructions,
            selectedPaymentMethod = form.paymentMethod
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = CartUiState()
    )

    fun toggleItemSelection(itemId: String) {
        _selectedItemIds.update { current ->
            if (current.contains(itemId)) {
                current - itemId
            } else {
                current + itemId
            }
        }
        _validationMessage.value = null
    }

    fun selectAll(select: Boolean) {
        if (select) {
            _selectedItemIds.value = repository.cart.value.map { it.id }.toSet()
        } else {
            _selectedItemIds.value = emptySet()
        }
        _validationMessage.value = null
    }

    fun addItem(product: Product, variant: ProductVariant, quantity: Int = 1): Boolean {
        val success = repository.addToCart(product, variant, quantity)
        if (success) {
            val itemId = "${product.id}_${variant.id}"
            _selectedItemIds.update { it + itemId }
        }
        return success
    }

    fun removeItem(itemId: String) {
        repository.removeFromCartById(itemId)
        _selectedItemIds.update { it - itemId }
    }

    fun removeSelectedItems() {
        val selected = _selectedItemIds.value
        if (selected.isNotEmpty()) {
            repository.removeItemsFromCart(selected)
            _selectedItemIds.value = emptySet()
        }
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        repository.updateCartQuantityById(itemId, newQuantity)
        if (newQuantity <= 0) {
            _selectedItemIds.update { it - itemId }
        }
    }

    fun increaseQuantity(itemId: String) {
        val item = repository.cart.value.find { it.id == itemId } ?: return
        if (item.quantity < item.selectedVariant.stock) {
            repository.updateCartQuantityById(itemId, item.quantity + 1)
        }
    }

    fun decreaseQuantity(itemId: String) {
        val item = repository.cart.value.find { it.id == itemId } ?: return
        if (item.quantity > 1) {
            repository.updateCartQuantityById(itemId, item.quantity - 1)
        } else {
            removeItem(itemId)
        }
    }

    fun clearCart() {
        repository.clearCart()
        _selectedItemIds.value = emptySet()
        _isCheckingOut.value = false
        _validationMessage.value = null
    }

    fun proceedToCheckout(): Boolean {
        val selectedCount = _selectedItemIds.value.size
        if (selectedCount == 0) {
            _validationMessage.value = "Please select at least 1 item from your cart to proceed to checkout."
            return false
        }
        _validationMessage.value = null
        _isCheckingOut.value = true
        return true
    }

    fun cancelCheckout() {
        _isCheckingOut.value = false
        _validationMessage.value = null
    }

    fun updateFullName(name: String) {
        _fullName.value = name
        _validationMessage.value = null
    }

    fun updatePhone(phone: String) {
        _phone.value = phone
        _validationMessage.value = null
    }

    fun updateAltPhone(phone: String) {
        _altPhone.value = phone
    }

    fun updateDistrict(district: String) {
        _district.value = district
    }

    fun updateCityTown(cityTown: String) {
        _cityTown.value = cityTown
        _validationMessage.value = null
    }

    fun updateAddress(address: String) {
        _address.value = address
        _validationMessage.value = null
    }

    fun updateDeliveryInstructions(instructions: String) {
        _deliveryInstructions.value = instructions
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    fun placeOrder() {
        val state = uiState.value
        val selectedItems = state.selectedCartItems
        if (selectedItems.isEmpty()) {
            _validationMessage.value = "Please select at least 1 item before placing your order."
            return
        }

        if (state.fullName.isBlank() || state.phone.isBlank() || state.address.isBlank()) {
            _validationMessage.value = "Please complete your full name, phone number, and delivery address."
            return
        }

        viewModelScope.launch {
            _isProcessingPayment.value = true
            _validationMessage.value = null
            delay(900)
            val order = repository.placeOrder(
                customerName = state.fullName,
                customerPhone = state.phone,
                district = state.district,
                cityTown = state.cityTown,
                deliveryAddress = state.address,
                paymentMethod = state.selectedPaymentMethod,
                itemsToCheckout = selectedItems
            )
            _placedOrder.value = order
            _isProcessingPayment.value = false
            _isCheckingOut.value = false
        }
    }

    fun dismissOrderReceipt() {
        _placedOrder.value = null
        _isCheckingOut.value = false
        _validationMessage.value = null
    }
}

private data class CheckoutFormState(
    val fullName: String,
    val phone: String,
    val altPhone: String,
    val district: String,
    val cityTown: String,
    val address: String,
    val deliveryInstructions: String,
    val paymentMethod: PaymentMethod
)

private data class CheckoutStatus(
    val isCheckingOut: Boolean,
    val isProcessingPayment: Boolean,
    val placedOrder: Order?,
    val validationMessage: String?
)
