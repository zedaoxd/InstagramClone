package com.example.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityEditPerfilBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class EditPerfilActivity extends AppCompatActivity {

    private ActivityEditPerfilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        binding = ActivityEditPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // disable the email field
        binding.textEmail.setFocusable(false);

        settingsToolbar();
        setDataCurrentUser();
        clickButtonSave();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void clickButtonSave(){
        binding.buttonSaveEditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = binding.textName.getText().toString();

                // save in authentication
                UserFirebase.updateUserName(newName);

                // save in database
                User user = UserFirebase.getDataCurrentUser();
                user.setName(newName);
                user.update();

                Toast.makeText(EditPerfilActivity.this, "Dados Alterados", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setDataCurrentUser(){
        FirebaseUser user = UserFirebase.getCurrentUserFirebase();
        binding.textName.setText(user.getDisplayName());
        binding.textEmail.setText(user.getEmail());
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.include.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }
}