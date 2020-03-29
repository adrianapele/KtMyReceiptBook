package com.example.ktmyreceiptbook.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Receipt::class], version = 1, exportSchema = false)
abstract class ReceiptDatabase: RoomDatabase()
{
    abstract fun receiptDao(): ReceiptDao

    companion object
    {
        private var instance: ReceiptDatabase? = null

        fun getInstance(context: Context): ReceiptDatabase
        {
            if (instance == null)
                synchronized(this)
                {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ReceiptDatabase::class.java,
                        "receipt_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }

            return instance as ReceiptDatabase
        }
    }
}