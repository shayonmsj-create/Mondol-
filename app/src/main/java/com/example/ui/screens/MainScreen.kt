package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.OrderDialog
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.theme.MondalGreen
import com.example.ui.theme.MondalLightGreen
import com.example.ui.viewmodel.MondalViewModel

@Composable
fun MainScreen(viewModel: MondalViewModel) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val orders by viewModel.allOrders.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val selectedProductForOrder by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val orderQuantity by viewModel.orderQuantity.collectAsStateWithLifecycle()
    val paymentMethod by viewModel.paymentMethod.collectAsStateWithLifecycle()
    val customerName by viewModel.customerName.collectAsStateWithLifecycle()
    val customerPhone by viewModel.customerPhone.collectAsStateWithLifecycle()
    val deliveryAddress by viewModel.deliveryAddress.collectAsStateWithLifecycle()
    val isSubmittingOrder by viewModel.isSubmittingOrder.collectAsStateWithLifecycle()
    val orderConfirmation by viewModel.orderConfirmation.collectAsStateWithLifecycle()

    val adminName by viewModel.adminProductName.collectAsStateWithLifecycle()
    val adminPrice by viewModel.adminProductPrice.collectAsStateWithLifecycle()
    val adminUnit by viewModel.adminProductUnit.collectAsStateWithLifecycle()
    val adminCategory by viewModel.adminProductCategory.collectAsStateWithLifecycle()
    val adminImage by viewModel.adminProductImage.collectAsStateWithLifecycle()
    val adminDesc by viewModel.adminProductDesc.collectAsStateWithLifecycle()

    val snackBarMessage by viewModel.snackBarMessage.collectAsStateWithLifecycle()

    // Handle Snackbar messages
    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackBarMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                // Tab 0: মন্ডল স্টোর
                NavigationBarItem(
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "মন্ডল স্টোর"
                        )
                    },
                    label = {
                        Text(
                            text = "মন্ডল স্টোর",
                            fontWeight = if (selectedIndex == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MondalGreen,
                        selectedTextColor = MondalGreen,
                        indicatorColor = MondalLightGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_shop")
                )

                // Tab 1: প্রোডাক্ট যোগ
                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddBusiness,
                            contentDescription = "প্রোডাক্ট যোগ"
                        )
                    },
                    label = {
                        Text(
                            text = "প্রোডাক্ট যোগ",
                            fontWeight = if (selectedIndex == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MondalGreen,
                        selectedTextColor = MondalGreen,
                        indicatorColor = MondalLightGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_admin")
                )

                // Tab 2: অর্ডার
                NavigationBarItem(
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    icon = {
                        if (orders.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = MondalGreen) {
                                        Text("${orders.size}", color = Color.White)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ListAlt,
                                    contentDescription = "অর্ডার"
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = "অর্ডার"
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "অর্ডার",
                            fontWeight = if (selectedIndex == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MondalGreen,
                        selectedTextColor = MondalGreen,
                        indicatorColor = MondalLightGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_orders")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                0 -> ShopScreen(
                    products = products,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onCategorySelect = viewModel::onCategorySelect,
                    onOrderClick = viewModel::openOrderDialog,
                    onNavigateToAdmin = { selectedIndex = 1 }
                )
                1 -> AdminScreen(
                    name = adminName,
                    price = adminPrice,
                    unit = adminUnit,
                    category = adminCategory,
                    imageUrl = adminImage,
                    description = adminDesc,
                    allProducts = allProducts,
                    onNameChange = viewModel::setAdminProductName,
                    onPriceChange = viewModel::setAdminProductPrice,
                    onUnitChange = viewModel::setAdminProductUnit,
                    onCategoryChange = viewModel::setAdminProductCategory,
                    onImageUrlChange = viewModel::setAdminProductImage,
                    onDescriptionChange = viewModel::setAdminProductDesc,
                    onAddProduct = { viewModel.addProduct() },
                    onDeleteProduct = viewModel::deleteProduct,
                    onRestoreSampleProducts = viewModel::restoreSampleProducts
                )
                2 -> OrdersScreen(
                    orders = orders,
                    onUpdateStatus = viewModel::updateOrderStatus,
                    onDeleteOrder = viewModel::deleteOrder,
                    onNavigateToShop = { selectedIndex = 0 }
                )
            }
        }
    }

    // Order Dialog
    selectedProductForOrder?.let { product ->
        OrderDialog(
            product = product,
            quantity = orderQuantity,
            paymentMethod = paymentMethod,
            customerName = customerName,
            customerPhone = customerPhone,
            deliveryAddress = deliveryAddress,
            isSubmitting = isSubmittingOrder,
            onQuantityChange = viewModel::setOrderQuantity,
            onPaymentMethodChange = viewModel::setPaymentMethod,
            onCustomerNameChange = viewModel::setCustomerName,
            onCustomerPhoneChange = viewModel::setCustomerPhone,
            onDeliveryAddressChange = viewModel::setDeliveryAddress,
            onConfirm = { viewModel.confirmOrder() },
            onDismiss = viewModel::closeOrderDialog
        )
    }

    // Order Success Dialog
    orderConfirmation?.let { order ->
        OrderSuccessDialog(
            order = order,
            onDismiss = viewModel::dismissOrderConfirmation,
            onViewOrders = {
                viewModel.dismissOrderConfirmation()
                selectedIndex = 2
            }
        )
    }
}
