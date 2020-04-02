package com.example.ktmyreceiptbook.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.model.Receipt

class ReceiptAdapter: ListAdapter<Receipt, ReceiptAdapter.ReceiptsViewHolder>(DiffCallback())
{
    lateinit var recyclerViewClickListener: RecyclerViewClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptsViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.receipts_list, null)

        return ReceiptsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptsViewHolder, position: Int)
    {
        val currentReceipt = getItem(position)

        holder.receiptTitleTextView.text = currentReceipt.title
        holder.receiptShortDescTextView.text = currentReceipt.shortDesc
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

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION)
                    recyclerViewClickListener.onRecyclerViewItemClick(itemView, getItem(position))
            }
        }
    }


    private class DiffCallback: DiffUtil.ItemCallback<Receipt>()
    {
        override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean
        {
            return oldItem.title == newItem.title &&
                    oldItem.shortDesc == newItem.shortDesc &&
                    oldItem.fullDesc == newItem.fullDesc &&
                    oldItem.imageUri == newItem.imageUri
        }
    }
}