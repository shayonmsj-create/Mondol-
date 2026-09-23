package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MondalDatabase : RoomDatabase() {

    abstract fun mondalDao(): MondalDao

    companion object {
        @Volatile
        private var INSTANCE: MondalDatabase? = null

        fun getDatabase(context: Context): MondalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MondalDatabase::class.java,
                    "mondal_enterprise.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
