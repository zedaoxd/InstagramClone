package com.example.instagramclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityMainBinding;
import com.example.instagramclone.util.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialSettings();

        setSupportActionBar(binding.includeToolbar.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_exit:
                logOutUser();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialSettings(){
        auth = FirebaseUtils.getFirebaseAuth();
    }

    private void logOutUser(){
        try {
            auth.signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}