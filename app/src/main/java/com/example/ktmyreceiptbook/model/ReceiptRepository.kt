package com.example.ktmyreceiptbook.model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class ReceiptRepository(application: Application)
{
    var receiptDao: ReceiptDao
    var allReceipts: LiveData<List<Receipt>>

    init
    {
        val database = ReceiptDatabase.getInstance(application)
        receiptDao = database.receiptDao()
        allReceipts = receiptDao.getAllReceipts()
    }

    fun insert(receipt: Receipt)
    {
        InsertReceiptAsyncTask(receiptDao).execute(receipt)
    }

    fun update(receipt: Receipt)
    {
        UpdateReceiptAsyncTask(receiptDao).execute(receipt)
    }

    fun delete(receipt: Receipt)
    {
        DeleteReceiptAsyncTask(receiptDao).execute(receipt)
    }

    fun deleteAllReceipts()
    {
        DeleteAllReceiptsAsyncTask(receiptDao).execute()
    }

    inner class InsertReceiptAsyncTask(val receiptDao: ReceiptDao): AsyncTask<Receipt, Void, Void>()
    {
        override fun doInBackground(vararg params: Receipt?): Void?
        {
            params[0]?.let { receiptDao.insert(it) }
            return null
        }
    }

    inner class UpdateReceiptAsyncTask(val receiptDao: ReceiptDao): AsyncTask<Receipt, Void, Void>()
    {
        override fun doInBackground(vararg params: Receipt?): Void?
        {
            params[0]?.let { receiptDao.update(it) }
            return null
        }
    }

    inner class DeleteReceiptAsyncTask(val receiptDao: ReceiptDao): AsyncTask<Receipt, Void, Void>()
    {
        override fun doInBackground(vararg params: Receipt?): Void?
        {
            params[0]?.let { receiptDao.delete(it) }
            return null
        }
    }

    inner class DeleteAllReceiptsAsyncTask(val receiptDao: ReceiptDao): AsyncTask<Void, Void, Void>()
    {
        override fun doInBackground(vararg params: Void?): Void?
        {
            receiptDao.deleteAllReceipts()
            return null
        }
    }
}