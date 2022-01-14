package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityRegisterBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.Utils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fieldName.requestFocus();
        binding.progressRegister.setVisibility(View.GONE);
        clickButtonRegister();
    }

    private void clickButtonRegister(){
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.fieldName.getText().toString();
                String email = binding.fieldEmail.getText().toString();
                String password = binding.fieldPassword.getText().toString();

                if (Utils.someEmptyFields(name, email, password)){
                    binding.progressRegister.setVisibility(View.GONE);
                    Utils.toastMessage(getApplicationContext(), StringUtils.emptyFields);
                } else {
                    binding.progressRegister.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    saveUserFirebase(user);
                }
            }
        });
    }

    private void saveUserFirebase(User user){

        binding.progressRegister.setVisibility(View.VISIBLE);
        FirebaseAuth auth = FirebaseUtils.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            try {

                                binding.progressRegister.setVisibility(View.GONE);

                                String uid = task.getResult().getUser().getUid();
                                user.setId(uid);
                                user.save();
                                UserFirebase.updateUserName(user.getName());

                                Utils.toastMessage(getApplicationContext(), StringUtils.registeredSuccessfully);
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            String exception;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                exception = StringUtils.weakPassword;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = StringUtils.invalidEmail;
                            } catch (FirebaseAuthUserCollisionException e){
                                exception = StringUtils.emailRegistered;
                            } catch (Exception e){
                                exception = StringUtils.errorToRegister;
                            }
                            Utils.toastMessage(getApplicationContext(), exception);

                        }
                    }
                });
    }
}