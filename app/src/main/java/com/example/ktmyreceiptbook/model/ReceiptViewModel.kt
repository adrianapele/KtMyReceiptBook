package com.example.ktmyreceiptbook.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReceiptViewModel(application: Application): AndroidViewModel(application)
{
    private val repository: ReceiptRepository = ReceiptRepository(application)
    var allReceipts: LiveData<List<Receipt>>
    var currentSelectedReceipt: MutableLiveData<Receipt> = MutableLiveData()

    init
    {
        allReceipts = repository.allReceipts
    }

    fun setCurrentSelectedReceipt(receipt: Receipt?)
    {
        currentSelectedReceipt.value = receipt
    }

    fun getCurrentSelectedReceipt(): Receipt? = currentSelectedReceipt.value

    fun insert(receipt: Receipt) = repository.insert(receipt)
    fun update(receipt: Receipt) = repository.update(receipt)
    fun delete(receipt: Receipt) = repository.delete(receipt)
    fun deleteAllReceipt() = repository.deleteAllReceipts()
}