package com.example.data.repository

import com.example.data.local.MondalDao
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class MondalRepository(private val dao: MondalDao) {

    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val allOrders: Flow<List<OrderEntity>> = dao.getAllOrders()

    suspend fun insertProduct(product: ProductEntity): Long = dao.insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)

    suspend fun deleteProductById(id: Long) = dao.deleteProductById(id)

    suspend fun insertOrder(order: OrderEntity): Long = dao.insertOrder(order)

    suspend fun updateOrderStatus(orderId: Long, newStatus: String) =
        dao.updateOrderStatus(orderId, newStatus)

    suspend fun deleteOrderById(id: Long) = dao.deleteOrderById(id)

    suspend fun seedInitialProductsIfEmpty() {
        if (dao.getProductCount() == 0) {
            dao.insertProducts(getSampleProducts())
        }
    }

    suspend fun restoreDefaultProducts() {
        dao.insertProducts(getSampleProducts())
    }

    companion object {
        fun getSampleProducts(): List<ProductEntity> = listOf(
            ProductEntity(
                name = "প্রিমিয়াম নাজিরশাইল চাল",
                price = 85.0,
                unit = "১ কেজি",
                category = "মুদি ও খাদ্য",
                imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=500&q=80",
                description = "ঝরঝরে ও সুস্বাদু ১ নম্বর গ্রেডের চাল, পলিশমুক্ত পুষ্টিকর।"
            ),
            ProductEntity(
                name = "খাঁটি ঘানিভাঙা সরিষার তেল",
                price = 250.0,
                unit = "১ লিটার",
                category = "তেল ও ঘি",
                imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&q=80",
                description = "১০০% খাঁটি দেশি সরিষার ঝাঁঝাল তেল, কোনো ভেজাল নেই।"
            ),
            ProductEntity(
                name = "সুন্দরবনের প্রাকৃতিক মধু",
                price = 650.0,
                unit = "৫০০ গ্রাম",
                category = "পুষ্টিকর খাদ্য",
                imageUrl = "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500&q=80",
                description = "প্রাকৃতিক চাকের তাজা মিষ্টি মধু, রোগ প্রতিরোধ ক্ষমতা বৃদ্ধি করে।"
            ),
            ProductEntity(
                name = "উন্নত জাতের বোরো ধান বীজ",
                price = 170.0,
                unit = "১ কেজি",
                category = "কৃষি ও বীজ",
                imageUrl = "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=500&q=80",
                description = "উচ্চ ফলনশীল প্রত্যয়িত ধান বীজ, মন্ডল এন্টারপ্রাইজ পরীক্ষিত।"
            ),
            ProductEntity(
                name = "দেশি চিকন মসুর ডাল",
                price = 140.0,
                unit = "১ কেজি",
                category = "মুদি ও খাদ্য",
                imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500&q=80",
                description = "সহজে সিদ্ধ হওয়া সুস্বাদু ও পুষ্টিকর লাল মসুর ডাল।"
            ),
            ProductEntity(
                name = "খাঁটি দেশি গাওয়া ঘি",
                price = 1150.0,
                unit = "৫০০ গ্রাম",
                category = "তেল ও ঘি",
                imageUrl = "https://images.unsplash.com/photo-1631451095765-2c91616fc9e6?w=500&q=80",
                description = "খাঁটি গাভীর দুধের ননী থেকে তৈরি সুগন্ধযুক্ত আসল গাওয়া ঘি।"
            ),
            ProductEntity(
                name = "দানাদার প্যাকেট চিনি",
                price = 135.0,
                unit = "১ কেজি",
                category = "মুদি ও খাদ্য",
                imageUrl = "https://images.unsplash.com/photo-1581441363689-1f3c3c414635?w=500&q=80",
                description = "ঝলমলে সাদা পরিষ্কার পরিচ্ছন্ন দানাদার রিফাইন্ড চিনি।"
            ),
            ProductEntity(
                name = "জৈব সার ও মাটির অনুখাদ্য",
                price = 160.0,
                unit = "২ কেজি",
                category = "কৃষি ও সার",
                imageUrl = "https://images.unsplash.com/photo-1615811361523-6bd03d7748e7?w=500&q=80",
                description = "ফসলের সতেজতা ও শিকড় মজবুত করতে উৎকৃষ্ট মানের জৈব সার।"
            )
        )
    }
}
