package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MondalDatabase
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.repository.MondalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

class MondalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MondalRepository

    val allProducts: StateFlow<List<ProductEntity>>
    val allOrders: StateFlow<List<OrderEntity>>

    // Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("সকল পণ্য")
    val selectedCategory = _selectedCategory.asStateFlow()

    // Filtered products flow
    val filteredProducts: StateFlow<List<ProductEntity>>

    // Order Dialog State
    private val _selectedProduct = MutableStateFlow<ProductEntity?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    private val _orderQuantity = MutableStateFlow(1)
    val orderQuantity = _orderQuantity.asStateFlow()

    private val _paymentMethod = MutableStateFlow("bKash")
    val paymentMethod = _paymentMethod.asStateFlow()

    private val _customerName = MutableStateFlow("")
    val customerName = _customerName.asStateFlow()

    private val _customerPhone = MutableStateFlow("")
    val customerPhone = _customerPhone.asStateFlow()

    private val _deliveryAddress = MutableStateFlow("")
    val deliveryAddress = _deliveryAddress.asStateFlow()

    private val _isSubmittingOrder = MutableStateFlow(false)
    val isSubmittingOrder = _isSubmittingOrder.asStateFlow()

    private val _orderConfirmation = MutableStateFlow<OrderEntity?>(null)
    val orderConfirmation = _orderConfirmation.asStateFlow()

    // Admin Add Product Form State
    private val _adminProductName = MutableStateFlow("")
    val adminProductName = _adminProductName.asStateFlow()

    private val _adminProductPrice = MutableStateFlow("")
    val adminProductPrice = _adminProductPrice.asStateFlow()

    private val _adminProductUnit = MutableStateFlow("১ কেজি")
    val adminProductUnit = _adminProductUnit.asStateFlow()

    private val _adminProductCategory = MutableStateFlow("মুদি ও খাদ্য")
    val adminProductCategory = _adminProductCategory.asStateFlow()

    private val _adminProductImage = MutableStateFlow("")
    val adminProductImage = _adminProductImage.asStateFlow()

    private val _adminProductDesc = MutableStateFlow("")
    val adminProductDesc = _adminProductDesc.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage = _snackBarMessage.asStateFlow()

    init {
        val dao = MondalDatabase.getDatabase(application).mondalDao()
        repository = MondalRepository(dao)

        allProducts = repository.allProducts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allOrders = repository.allOrders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        filteredProducts = combine(allProducts, _searchQuery, _selectedCategory) { products, query, cat ->
            products.filter { product ->
                val matchesCategory = if (cat == "সকল পণ্য") true else product.category == cat
                val matchesQuery = query.isBlank() ||
                        product.name.contains(query, ignoreCase = true) ||
                        product.category.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
                matchesCategory && matchesQuery
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial products if brand new install
        viewModelScope.launch {
            repository.seedInitialProductsIfEmpty()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun openOrderDialog(product: ProductEntity) {
        _selectedProduct.value = product
        _orderQuantity.value = 1
        _paymentMethod.value = "bKash"
    }

    fun closeOrderDialog() {
        _selectedProduct.value = null
    }

    fun setOrderQuantity(qty: Int) {
        if (qty in 1..99) {
            _orderQuantity.value = qty
        }
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    fun setCustomerName(name: String) {
        _customerName.value = name
    }

    fun setCustomerPhone(phone: String) {
        _customerPhone.value = phone
    }

    fun setDeliveryAddress(address: String) {
        _deliveryAddress.value = address
    }

    fun dismissOrderConfirmation() {
        _orderConfirmation.value = null
    }

    fun confirmOrder(onComplete: (Boolean) -> Unit = {}) {
        val product = _selectedProduct.value ?: return
        val qty = _orderQuantity.value
        val subtotal = product.price * qty
        val deliveryFee = if (subtotal >= 500.0) 0.0 else 40.0
        val total = subtotal + deliveryFee

        val orderNum = "ME-" + String.format(Locale.US, "%05d", Random.nextInt(10000, 99999))

        val order = OrderEntity(
            orderNumber = orderNum,
            productName = product.name,
            quantity = qty,
            unitPrice = product.price,
            deliveryFee = deliveryFee,
            totalPrice = total,
            paymentMethod = _paymentMethod.value,
            customerName = _customerName.value.ifBlank { "সম্মানিত গ্রাহক" },
            customerPhone = _customerPhone.value.ifBlank { "017XXXXXXXX" },
            deliveryAddress = _deliveryAddress.value.ifBlank { "ক্যাশ অন ডেলিভারি এরিয়া" },
            status = "অপেক্ষমান",
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            _isSubmittingOrder.value = true
            repository.insertOrder(order)
            _isSubmittingOrder.value = false
            _selectedProduct.value = null
            _orderConfirmation.value = order
            _snackBarMessage.value = "মন্ডল এন্টারপ্রাইজে অর্ডার সফল!"
            onComplete(true)
        }
    }

    // --- Admin Add Product ---
    fun setAdminProductName(name: String) { _adminProductName.value = name }
    fun setAdminProductPrice(price: String) { _adminProductPrice.value = price }
    fun setAdminProductUnit(unit: String) { _adminProductUnit.value = unit }
    fun setAdminProductCategory(cat: String) { _adminProductCategory.value = cat }
    fun setAdminProductImage(url: String) { _adminProductImage.value = url }
    fun setAdminProductDesc(desc: String) { _adminProductDesc.value = desc }

    fun addProduct(onSuccess: () -> Unit = {}) {
        val name = _adminProductName.value.trim()
        val priceStr = _adminProductPrice.value.trim()
        if (name.isBlank() || priceStr.isBlank()) {
            _snackBarMessage.value = "দয়া করে প্রোডাক্ট নাম ও সঠিক দাম দিন"
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val defaultImg = when (_adminProductCategory.value) {
            "তেল ও ঘি" -> "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&q=80"
            "কৃষি ও বীজ" -> "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=500&q=80"
            "পুষ্টিকর খাদ্য" -> "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500&q=80"
            else -> "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&q=80"
        }

        val newProduct = ProductEntity(
            name = name,
            price = price,
            unit = _adminProductUnit.value.ifBlank { "১ কেজি" },
            category = _adminProductCategory.value.ifBlank { "মুদি ও খাদ্য" },
            imageUrl = _adminProductImage.value.ifBlank { defaultImg },
            description = _adminProductDesc.value.ifBlank { "মন্ডল এন্টারপ্রাইজের খাঁটি পণ্য" }
        )

        viewModelScope.launch {
            repository.insertProduct(newProduct)
            _adminProductName.value = ""
            _adminProductPrice.value = ""
            _adminProductImage.value = ""
            _adminProductDesc.value = ""
            _snackBarMessage.value = "মন্ডল এন্টারপ্রাইজে প্রোডাক্ট যোগ হয়েছে"
            onSuccess()
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProductById(id)
            _snackBarMessage.value = "প্রোডাক্ট সফলভাবে মুছে ফেলা হয়েছে"
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            _snackBarMessage.value = "অর্ডার স্ট্যাটাস আপডেট: $newStatus"
        }
    }

    fun deleteOrder(id: Long) {
        viewModelScope.launch {
            repository.deleteOrderById(id)
            _snackBarMessage.value = "অর্ডার তালিকা থেকে বাদ দেওয়া হয়েছে"
        }
    }

    fun restoreSampleProducts() {
        viewModelScope.launch {
            repository.restoreDefaultProducts()
            _snackBarMessage.value = "নমুনা প্রোডাক্টগুলো পুনরায় লোড হয়েছে"
        }
    }

    fun clearSnackBarMessage() {
        _snackBarMessage.value = null
    }
}
