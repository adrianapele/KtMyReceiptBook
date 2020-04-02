package com.example.ktmyreceiptbook.adapter

import android.view.View
import com.example.ktmyreceiptbook.model.Receipt

interface RecyclerViewClickListener
{
    fun onRecyclerViewItemClick(view: View, receipt: Receipt)
}