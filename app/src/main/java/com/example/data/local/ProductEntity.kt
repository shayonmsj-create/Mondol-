package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mondal_products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val unit: String = "১ কেজি",
    val category: String = "মুদি ও খাদ্য",
    val imageUrl: String = "",
    val description: String = "১০০% খাঁটি ও গুণগত মানসম্পন্ন পণ্য",
    val inStock: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
