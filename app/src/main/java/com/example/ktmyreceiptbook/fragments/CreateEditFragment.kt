package com.example.ktmyreceiptbook.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ktmyreceiptbook.R
import com.example.ktmyreceiptbook.model.Receipt
import com.example.ktmyreceiptbook.model.ReceiptViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val CREATE_EDIT_FRAGMENT_TAG = "createEditFragment"

const val REQUEST_ADD = "requestAdd"
const val REQUEST_EDIT = "requestEdit"

private const val CHOOSE_PHOTO_FROM_GALLERY_OPTION = "Choose from Gallery"
private const val TAKE_PHOTO_OPTION = "Take Photo"
private const val CANCEL_OPTION = "Cancel"

private const val PICK_IMAGE_REQUEST = 1
private const val TAKE_PHOTO_REQUEST = 2

private const val CAMERA_PERMISSION_REQUEST_CODE = 100
private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 110

class CreateEditFragment : Fragment()
{
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
        val rootView = inflater.inflate(R.layout.fragment_create_edit, container, false)

        titleEditText = rootView.findViewById(R.id.create_edit_title_id)
        shortDescEditText = rootView.findViewById(R.id.create_edit_short_desc_text_id)
        fullDescEditText = rootView.findViewById(R.id.create_edit_full_desc_text_id)
        imageView = rootView.findViewById(R.id.create_edit_image_id)
        imageView.setOnClickListener(this::selectImage)

        receiptViewModel = ViewModelProvider(this)[ReceiptViewModel::class.java]
        val currentSelectedReceipt: Receipt? = receiptViewModel.currentSelectedReceipt.value

        if (currentSelectedReceipt != null && REQUEST_EDIT == requestType)
        {
            titleEditText.setText(currentSelectedReceipt.title)
            shortDescEditText.setText(currentSelectedReceipt.shortDesc)
            fullDescEditText.setText(currentSelectedReceipt.fullDesc)

            imagePath = currentSelectedReceipt.imageUri
            imageView.setImageURI(Uri.parse(imagePath))
        }

        if (REQUEST_EDIT == requestType)
            activity?.title = "Edit Receipt"
        else
            activity?.title = "Add Receipt"

        return rootView
    }

    private fun selectImage(view: View)
    {
        val options = arrayOf(CHOOSE_PHOTO_FROM_GALLERY_OPTION,
        TAKE_PHOTO_OPTION, CANCEL_OPTION)

        AlertDialog.Builder(view.context)
            .setTitle("Choose receipt picture")
            .setItems(options) { dialog, which ->
                val option: CharSequence = options[which]

                when {
                    TAKE_PHOTO_OPTION == option -> takePhotoWithPermissions(view.context)
                    CHOOSE_PHOTO_FROM_GALLERY_OPTION == option -> choosePhotoFromGalleryWithPermissions(view.context)
                    CANCEL_OPTION == option -> dialog.dismiss()
                }
            }
            .create()
            .show()
    }

    private fun takePhotoWithPermissions(context: Context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val cameraPermission = context.checkSelfPermission(Manifest.permission.CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                {
                    AlertDialog.Builder(context)
                        .setMessage("You need to allow access to Camera in order to be able to take photos")
                        .setPositiveButton("OKAY") { _, _ ->
                            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show()

                    return
                }
                else
                {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                    return
                }
            }
        }

        takePhotos(context)
    }

    private fun takePhotos(context: Context)
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(context.packageManager) != null)
        {
            var photoFile: File? = null

            try
            {
                photoFile = createImageFile(context)
            }
            catch (exception: IOException)
            {
                Toast.makeText(context, "Could not create image", Toast.LENGTH_SHORT).show()
            }

            var photoURI: Uri? = null

            if (photoFile != null)
            {
                try
                {
                    photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile)
                }
                catch (exception: IllegalArgumentException)
                {
                    Toast.makeText(context, "Could not retrieve image from internal storage", Toast.LENGTH_SHORT).show()
                }

                if (photoURI != null)
                {
                    imagePath = photoURI.toString()

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
                }
            }
        }
    }

    private fun createImageFile(context: Context): File
    {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "jpg_" +  timeStamp + "_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun choosePhotoFromGalleryWithPermissions(context: Context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val readExternalStoragePermission = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    AlertDialog.Builder(context)
                        .setMessage("You need to allow access to your storage in order to be able to take and choose photos")
                        .setPositiveButton("OKAY") { _, _ ->
                            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_CODE)
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show()

                    return
                }
                else
                {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION_CODE)
                    return
                }
            }
        }

        choosePhotoFromGallery(context)
    }

    private fun choosePhotoFromGallery(context: Context)
    {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        if (intent.resolveActivity(context.packageManager) != null)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK)
        {
            val selectedImage: Uri? = data?.data

            if (selectedImage != null)
            {
                val path: String? = getPathFromUri(selectedImage)
                val file = File(path!!)
                imagePath = Uri.fromFile(file).toString()
                imageView.setImageURI(Uri.parse(imagePath))
            }
            else
                Toast.makeText(context, "Could not load image", Toast.LENGTH_SHORT).show()
        }
        else if (requestCode == TAKE_PHOTO_REQUEST && resultCode == Activity.RESULT_OK)
            imageView.setImageURI(Uri.parse(imagePath))
    }

    private fun getPathFromUri(contentUri: Uri): String?
    {
        val selection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.contentResolver?.query(contentUri, null, null, null)

        if (cursor?.moveToFirst()!!)
        {
            val columnIndex = cursor.getColumnIndex(selection[0])
            return cursor.getString(columnIndex)
        }

        cursor.close()
        return null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                takePhotos(context!!)
            else
                Toast.makeText(context, "Camera Permission Not Granted", Toast.LENGTH_SHORT).show()
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                choosePhotoFromGallery(context!!)
            else
                Toast.makeText(context, "Read External Storage Permission Not Granted", Toast.LENGTH_SHORT).show()
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.save_receipt)
        {
            saveReceipt()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveReceipt()
    {
        val title = titleEditText.text.toString()
        val shortDesc = shortDescEditText.text.toString()
        val fullDesc = fullDescEditText.text.toString()

        if (title.trim().isEmpty() || shortDesc.trim().isEmpty() || fullDesc.trim().isEmpty())
        {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        else if (imagePath.isEmpty())
        {
            Toast.makeText(context, "Please add an image", Toast.LENGTH_SHORT).show()
            return
        }

        val currentSelectedReceipt = receiptViewModel.currentSelectedReceipt.value

        if (requestType == REQUEST_ADD && currentSelectedReceipt == null)
        {
            val receipt = Receipt(0, title, shortDesc, fullDesc, imagePath)
            receiptViewModel.insert(receipt)

            Toast.makeText(context, "Receipt saved", Toast.LENGTH_SHORT).show()
        }
        else if (requestType == REQUEST_EDIT && currentSelectedReceipt != null)
        {
            val receipt = Receipt(currentSelectedReceipt.id, title, shortDesc, fullDesc, imagePath)
            receiptViewModel.update(receipt)
            receiptViewModel.setCurrentSelectedReceipt(receipt)

            Toast.makeText(context, "Receipt updated", Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(context, "Receipt could not be created", Toast.LENGTH_SHORT).show()

        activity?.onBackPressed()
    }






































}
