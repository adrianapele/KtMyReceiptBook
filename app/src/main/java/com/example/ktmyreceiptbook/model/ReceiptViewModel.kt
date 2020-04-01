package com.example.ktmyreceiptbook.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReceiptViewModel(application: Application): AndroidViewModel(application)
{
    var repository: ReceiptRepository = ReceiptRepository(application)
    lateinit var allReceipts: LiveData<List<Receipt>>
    val currentSelectedReceipt: MutableLiveData<Receipt> = MutableLiveData()

    init
    {
        allReceipts = repository.allReceipts
    }

    fun setCurrentSelectedReceipt(receipt: Receipt) = currentSelectedReceipt.postValue(receipt)

    fun insert(receipt: Receipt) = repository.insert(receipt)
    fun update(receipt: Receipt) = repository.update(receipt)
    fun delete(receipt: Receipt) = repository.delete(receipt)
    fun deleteAllReceipt() = repository.deleteAllReceipts()
}