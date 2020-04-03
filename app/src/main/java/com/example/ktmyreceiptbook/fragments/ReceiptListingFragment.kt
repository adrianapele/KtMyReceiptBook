package com.example.ktmyreceiptbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.adapter.MyRecyclerView
import com.example.ktmyreceiptbook.adapter.ReceiptAdapter
import com.example.ktmyreceiptbook.model.ReceiptViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val LISTING_FRAGMENT_TAG = "listingFragment"

class ReceiptListingFragment : Fragment()
{
    private lateinit var receiptViewModel: ReceiptViewModel
    private lateinit var myRecyclerView: MyRecyclerView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView = inflater.inflate(R.layout.fragment_receipt_listing, container, false)

        myRecyclerView = rootView.findViewById(R.id.recycler_view_id)
        myRecyclerView.layoutManager = LinearLayoutManager(context)
        myRecyclerView.setHasFixedSize(true)

        var emptyView = rootView.findViewById<RelativeLayout>(R.id.empty_view_id)
        myRecyclerView.view = emptyView

        var adapter = ReceiptAdapter()
        adapter.recyclerViewClickListener
        myRecyclerView.adapter = adapter

//        receiptViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(ReceiptViewModel::class, activity)
//        receiptViewModel.allReceipts.observe(viewLifecycleOwner, adapter::submitList)
//
//        var fab = rootView.findViewById<FloatingActionButton>(R.id.fab_add_btn_id)
//        fab.setOnClickListener()

        activity?.title = "My Receipts"

        return rootView
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ReceiptListingFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ReceiptListingFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}
