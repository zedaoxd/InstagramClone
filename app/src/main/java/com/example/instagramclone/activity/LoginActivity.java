package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityLoginBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fieldEmail.requestFocus();
        binding.progressLogin.setVisibility(View.GONE);

        clickButtonRegister();
        clickButtonLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isThereAnyUser()){

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        }
    }

    private boolean isThereAnyUser(){
        auth = FirebaseUtils.getFirebaseAuth();
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    private void clickButtonRegister(){
        binding.textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });
    }

    private void clickButtonLogin(){
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.fieldEmail.getText().toString();
                String password = binding.fieldPassword.getText().toString();

                if (Utils.someEmptyFields(email, password)){

                    Utils.toastMessage(getApplicationContext(), StringUtils.emptyFields);

                } else {

                    binding.progressLogin.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    login(user);
                }
            }
        });
    }

    private void login(User user){
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        binding.progressLogin.setVisibility(View.GONE);
                        if (task.isSuccessful()){

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            Utils.toastMessage(getApplicationContext(), StringUtils.loginSuccess);
                            finish();

                        } else {

                            String message;
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException e){
                                message = StringUtils.unregisteredUser;
                            } catch (FirebaseAuthInvalidCredentialsException e){
                                message = StringUtils.incorrectEmailOrPassword;
                            } catch (Exception e){
                                message = StringUtils.errorLoggingInUser;
                            }
                            Utils.toastMessage(getApplicationContext(), message);

                        }
                    }
                });
    }
}