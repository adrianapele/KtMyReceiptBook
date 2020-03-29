package com.example.myreceiptbook.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myreceiptbook.R;
import com.example.myreceiptbook.model.Receipt;
import com.example.myreceiptbook.model.ReceiptViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class CreateEditFragment extends Fragment
{
    static final String CREATE_EDIT_FRAGMENT_TAG = "createEditFragment";

    static final String REQUEST_ADD = "requestAdd";
    static final String REQUEST_EDIT = "requestEdit";

    private static final String CHOOSE_PHOTO_FROM_GALLERY_OPTION = "Choose from Gallery";
    private static final String TAKE_PHOTO_OPTION = "Take Photo";
    private static final String CANCEL_OPTION = "Cancel";

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private ImageView imageView;
    private EditText titleEditText;
    private EditText shortDescEditText;
    private EditText longDescEditText;

    private ReceiptViewModel receiptViewModel;
    private String requestType;

    private String imagePath;

    public CreateEditFragment()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_create_edit, container, false);

        titleEditText = rootView.findViewById(R.id.createEditTitleEditTextId);
        shortDescEditText = rootView.findViewById(R.id.createEditShortDescEditTextId);
        longDescEditText = rootView.findViewById(R.id.createEditLongDescEditTextId);
        imageView = rootView.findViewById(R.id.createEditImageViewId);
        imageView.setOnClickListener(v -> selectImage(v.getContext()));

        receiptViewModel = ViewModelProviders.of(getActivity()).get(ReceiptViewModel.class);
        final Receipt currentSelectedReceipt = receiptViewModel.getCurrentSelectedReceipt().getValue();

        if (currentSelectedReceipt != null && REQUEST_EDIT.equals(requestType))
        {
            titleEditText.setText(currentSelectedReceipt.getTitle());
            shortDescEditText.setText(currentSelectedReceipt.getShortDescription());
            longDescEditText.setText(currentSelectedReceipt.getLargeDescription());

            Picasso.with(getContext())
                    .load(Uri.parse(currentSelectedReceipt.getImageUri()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView);
        }

        if (REQUEST_EDIT.equals(requestType))
            getActivity().setTitle("Edit Receipt");
        else
            getActivity().setTitle("Add Receipt");

        return rootView;
    }

    private void selectImage(Context context)
    {
        CharSequence[] options = {CHOOSE_PHOTO_FROM_GALLERY_OPTION,
        TAKE_PHOTO_OPTION, CANCEL_OPTION};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Choose receipt picture");
        alertDialogBuilder.setItems(options, (dialog, which) ->
        {
            final CharSequence option = options[which];

            if (TAKE_PHOTO_OPTION.contentEquals(option))
                takePhotoWithPermissions(context);
            else if (CHOOSE_PHOTO_FROM_GALLERY_OPTION.contentEquals(option))
                choosePhotoFromGallery(context);
            else if (CANCEL_OPTION.contentEquals(option))
                dialog.dismiss();
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void takePhotoWithPermissions(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            final int cameraPermission = context.checkSelfPermission(Manifest.permission.CAMERA);
            if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            {
             