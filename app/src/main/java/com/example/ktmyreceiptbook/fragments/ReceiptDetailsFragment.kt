package com.example.ktmyreceiptbook.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.model.Receipt
import com.example.ktmyreceiptbook.model.ReceiptViewModel
import com.github.chrisbanes.photoview.PhotoView

const val DETAILS_FRAGMENT_TAG = "detailsFragment"

class ReceiptDetailsFragment : Fragment()
{
    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var fullDescTextView: TextView

    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

    private lateinit var receiptViewModel: ReceiptViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val rootView = inflater.inflate(R.layout.fragment_receipt_details, container, false)

        imageView = rootView.findViewById(R.id.details_image_id)
        titleTextView = rootView.findViewById(R.id.details_title_id)
        fullDescTextView = rootView.findViewById(R.id.details_full_desc_id)

        editButton = rootView.findViewById(R.id.edit_button_id)
        editButton.setOnClickListener(this::openCreateEditFragment)

        deleteButton = rootView.findViewById(R.id.delete_button_id)
        deleteButton.setOnClickListener(this::openDeleteDialog)

        receiptViewModel = ViewModelProvider(this)[ReceiptViewModel::class.java]
        receiptViewModel.currentSelectedReceipt.observe(viewLifecycleOwner, Observer<Receipt>
        { receipt: Receipt ->
            titleTextView.text = receipt.title
            fullDescTextView.text = receipt.fullDesc
            imageView.setImageURI(Uri.parse(receipt.imageUri))
            imageView.setOnClickListener { openFullSizeImageWithPinch(receipt) }
        })

        activity?.title = "Receipt Details"

        return rootView
    }

    private fun openCreateEditFragment(view: View)
    {
        val activity: AppCompatActivity = view.context as AppCompatActivity
        val supportFragmentManager:FragmentManager = activity.supportFragmentManager

        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(CREATE_EDIT_FRAGMENT_TAG)
        if (fragment == null)
            fragment = CreateEditFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, CREATE_EDIT_FRAGMENT_TAG)
            .addToBackStack(CREATE_EDIT_FRAGMENT_TAG)
            .commit()
    }

    private fun openDeleteDialog(view: View)
    {
        AlertDialog.Builder(view.context)
            .setTitle("Delete")
            .setMessage("Do you want to delete this receipt?")
            .setNegativeButton("Cancel") { dialog, _ ->  dialog.dismiss()}
            .setPositiveButton("OKAY") { _, _ ->
                receiptViewModel.delete(receiptViewModel.currentSelectedReceipt.value!!)
                receiptViewModel.currentSelectedReceipt.value = null

                activity?.onBackPressed()
            }
            .create()
            .show()
    }

    private fun openFullSizeImageWithPinch(receipt: Receipt)
    {
        val imageContainerView: View = layoutInflater.inflate(R.layout.image_zoomable_layout, null)
        val photoView: PhotoView = imageContainerView.findViewById(R.id.zoomable_image_id)
        photoView.setImageURI(Uri.parse(receipt.imageUri))

        AlertDialog.Builder(activity)
            .setView(imageContainerView)
            .create()
            .show()
    }
}
