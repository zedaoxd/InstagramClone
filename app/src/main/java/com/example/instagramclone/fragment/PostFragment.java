package com.example.instagramclone.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.activity.FilterActivity;
import com.example.instagramclone.databinding.FragmentPostBinding;
import com.example.instagramclone.util.Permission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private final int CAMERA = 100;
    private final int GALLERY = 200;

    private String[] permitions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Permission.validatePermissions(permitions, getActivity(), 1);

        clickButtonOpenCamera();
        clickButtonOpenGallery();
        return view;
    }

    private void clickButtonOpenGallery(){
        binding.openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //i.putExtra("action", GALLERY);

                if (i.resolveActivity(getActivity().getPackageManager()) != null){
                    startGallery.launch(i);
                }
            }
        });
    }

    private void clickButtonOpenCamera(){
        binding.openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //i.putExtra("action", CAMERA);

                if (i.resolveActivity(getActivity().getPackageManager()) != null){
                    startCamera.launch(i);
                }
            }
        });
    }


    private ActivityResultLauncher<Intent> startGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){


                try {

                    Bitmap image = null;
                    Intent data = result.getData();
                    Uri uri = data.getData();
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    startIntentFilter(image);

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }
    });

    private ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){

                        Intent data = result.getData();
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        startIntentFilter(image);

                    }
                }
            }
    );

    private void startIntentFilter(Bitmap image){
        if (image != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] dataImage = baos.toByteArray();

            // send image for filter application
            Intent i = new Intent(getActivity(), FilterActivity.class);
            i.putExtra("photo", dataImage);
            startActivity(i);
        }
    }
}