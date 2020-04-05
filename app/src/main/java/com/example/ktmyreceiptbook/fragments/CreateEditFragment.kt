package com.example.ktmyreceiptbook.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.model.ReceiptViewModel

const val CREATE_EDIT_FRAGMENT_TAG = "createEditFragment"

const val REQUEST_ADD = "requestAdd"
const val REQUEST_EDIT = "requestEdit"

class CreateEditFragment : Fragment()
{
    private val CHOOSE_PHOTO_FROM_GALLERY_OPTION = "Choose from Gallery"
    private val TAKE_PHOTO_OPTION = "Take Photo"
    private val CANCEL_OPTION = "Cancel"

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 110

    lateinit var imageView: ImageView
    lateinit var titleEditText: EditText
    lateinit var shortDescEditText: EditText
    lateinit var fullDescEditText: EditText

    lateinit var receiptViewModel: ReceiptViewModel

    lateinit var requestType: String
    lateinit var imagePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_edit, container, false)
    }

}
