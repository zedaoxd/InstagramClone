package com.example.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityFilterBinding;

public class FilterActivity extends AppCompatActivity {

    private ActivityFilterBinding binding;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            byte[] dataImage = bundle.getByteArray("photo");
            image = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
            binding.imagePhotoFilter.setImageBitmap(image);
        }

    }
}