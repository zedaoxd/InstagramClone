package com.example.instagramclone.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityEditPerfilBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class EditPerfilActivity extends AppCompatActivity {

    private ActivityEditPerfilBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private User user;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        binding = ActivityEditPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // disable the email field
        binding.textEmail.setFocusable(false);

        user = UserFirebase.getDataCurrentUser();
        storageReference = FirebaseUtils.getFirebaseStorage();

        activityResultLauncher = launcher();

        settingsToolbar();
        setDataCurrentUser();
        clickButtonSave();
        clickButtonSelectPhoto();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private ActivityResultLauncher<Intent> launcher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    try {

                        Uri url = result.getData().getData();
                        Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), url);
                        if (image != null) {
                            binding.imagePerfilEdit.setImageBitmap(image);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                            byte[] dataImage = baos.toByteArray();

                            StorageReference imageRef = storageReference
                                    .child("images")
                                    .child("perfil")
                                    .child(user.getId() + ".jpeg");

                            UploadTask uploadTask = imageRef.putBytes(dataImage);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditPerfilActivity.this,
                                            "Erro ao fazer upload da image",
                                            Toast.LENGTH_LONG).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(EditPerfilActivity.this,
                                            "Sucesso ao fazer upload da image",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    private void clickButtonSelectPhoto(){
        binding.textChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null){
                    activityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void clickButtonSave(){
        binding.buttonSaveEditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = binding.textName.getText().toString();

                // save in authentication
                UserFirebase.updateUserName(newName);

                // save in database
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