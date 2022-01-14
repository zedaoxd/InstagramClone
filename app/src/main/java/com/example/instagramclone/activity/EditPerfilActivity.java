package com.example.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityEditPerfilBinding;

import java.util.Objects;

public class EditPerfilActivity extends AppCompatActivity {

    private ActivityEditPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        binding = ActivityEditPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingsToolbar();
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.include.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }
}