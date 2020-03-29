package com.example.ktmyreceiptbook.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReceiptDao
{
    @Insert
    fun insert(receipt: Receipt)

    @Update
    fun update(receipt: Receipt)

    @Delete
    fun delete(receipt: Receipt)

    @Query("DELETE FROM receipt_table")
    fun deleteAllReceipts()

    @Query("SELECT * FROM receipt_table")
    fun getAllReceipts(): LiveData<List<Receipt>>
}