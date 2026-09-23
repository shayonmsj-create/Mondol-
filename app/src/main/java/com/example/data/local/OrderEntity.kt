package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mondal_orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val productName: String,
    val quantity: Int = 1,
    val unitPrice: Double,
    val deliveryFee: Double = 0.0,
    val totalPrice: Double,
    val paymentMethod: String, // "bKash", "Nagad", "COD"
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val status: String = "অপেক্ষমান", // "অপেক্ষমান", "প্রসেসিং", "ডেলিভারড"
    val timestamp: Long = System.currentTimeMillis()
)
