package com.example.ktmyreceiptbook.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.model.Receipt

class ReceiptAdapter: ListAdapter<Receipt, ReceiptAdapter.ReceiptsViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptsViewHolder
    {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.receipts_list, null)

        return ReceiptsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptsViewHolder, position: Int)
    {
        var currentReceipt = getItem(position)

        holder.receiptTitleTextView.setText(currentReceipt.title)
        holder.receiptShortDescTextView.setText(currentReceipt.shortDesc)
        holder.receiptImageView.setImageURI(Uri.parse(currentReceipt.imageUri))
    }


    inner class ReceiptsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var receiptImageView: ImageView
        var receiptTitleTextView: TextView
        var receiptShortDescTextView: TextView

        init
        {
            receiptImageView = itemView.findViewById(R.id.listing_receipt_image_id)
            receiptTitleTextView = itemView.findViewById(R.id.listing_receipt_title_id)
            receiptShortDescTextView = itemView.findViewById(R.id.listing_receipt_short_desc_id)

            itemView.setOnClickListener(View.OnClickListener
            {

            })
        }
    }
}