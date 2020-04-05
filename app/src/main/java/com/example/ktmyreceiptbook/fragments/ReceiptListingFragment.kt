package com.example.ktmyreceiptbook.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.adapter.MyRecyclerView
import com.example.ktmyreceiptbook.adapter.ReceiptAdapter
import com.example.ktmyreceiptbook.adapter.RecyclerViewClickListener
import com.example.ktmyreceiptbook.model.Receipt
import com.example.ktmyreceiptbook.model.ReceiptViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

const val LISTING_FRAGMENT_TAG = "listingFragment"

class ReceiptListingFragment: Fragment(), RecyclerViewClickListener
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
        myRecyclerView.layoutManager = LinearLayoutManager(activity)
        myRecyclerView.setHasFixedSize(true)

        val emptyView = rootView.findViewById<RelativeLayout>(R.id.empty_view_id)
        myRecyclerView.setEmptyView(emptyView)

        val adapter = ReceiptAdapter()
        adapter.recyclerViewClickListener = this
        myRecyclerView.adapter = adapter

        val receiptsObserver = Observer<List<Receipt>>
        {
            adapter.submitList(it)
        }
        receiptViewModel = ViewModelProvider(this)[ReceiptViewModel::class.java]
        receiptViewModel.allReceipts.observe(viewLifecycleOwner, receiptsObserver)

        val fab = rootView.findViewById<FloatingActionButton>(R.id.fab_add_btn_id)
        fab.setOnClickListener(this::openCreateEditFragment)

        activity?.title = "My Receipts"

        return rootView
    }

    private fun openCreateEditFragment(view: View)
    {
        val activity = view.context as AppCompatActivity
        val supportFragmentManager = activity.supportFragmentManager
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(CREATE_EDIT_FRAGMENT_TAG)

        if (fragment == null)
            fragment = CreateEditFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CREATE_EDIT_FRAGMENT_TAG)
            .addToBackStack(CREATE_EDIT_FRAGMENT_TAG)
            .commit()

        receiptViewModel.setCurrentSelectedReceipt(null)
    }

    override fun onRecyclerViewItemClick(view: View, receipt: Receipt)
    {
        openDetailsFragment(view, receipt)
    }

    private fun openDetailsFragment(view: View, receipt: Receipt)
    {
        val activity: AppCompatActivity = view.context as AppCompatActivity
        val supportFragmentManager: FragmentManager = activity.supportFragmentManager
        var detailsFragment: Fragment? = supportFragmentManager.findFragmentByTag(DETAILS_FRAGMENT_TAG)

        if (detailsFragment == null)
            detailsFragment = ReceiptDetailsFragment()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fragment_container, detailsFragment, DETAILS_FRAGMENT_TAG)
            .addToBackStack(DETAILS_FRAGMENT_TAG)
            .commit()

        receiptViewModel.setCurrentSelectedReceipt(receipt)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.delete_all_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.delete_all_receipts)
        {
            val receipts = receiptViewModel.allReceipts.value
            if (receipts?.size == 0)
                Toast.makeText(context, "You don't have receipts to delete", Toast.LENGTH_SHORT).show()
            else
            {
                receiptViewModel.deleteAllReceipt()
                Toast.makeText(context, "All receipts deleted", Toast.LENGTH_SHORT).show()
            }

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
