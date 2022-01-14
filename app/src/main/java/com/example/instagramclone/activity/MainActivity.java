package com.example.instagramclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityMainBinding;
import com.example.instagramclone.fragment.FeedFragment;
import com.example.instagramclone.fragment.PerfilFragment;
import com.example.instagramclone.fragment.PostFragment;
import com.example.instagramclone.fragment.SearchFragment;
import com.example.instagramclone.util.FirebaseUtils;
import com.google.android.material.navigation.NavigationBarView;
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
        setSupportActionBar(binding.includeToolbar.toolbar);
        initialSettings();

        clickBottomNavigation();
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
        settingsBottomNavigation();
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

    private void settingsBottomNavigation(){
        Menu menu = binding.includeBottomNavigation.bnve.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        switchFragment(new FeedFragment());
    }

    private void clickBottomNavigation()
    {
        binding.includeBottomNavigation.bnve.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_home:
                        switchFragment(new FeedFragment());
                        break;
                    case R.id.menu_search:
                        switchFragment(new SearchFragment());
                        break;
                    case R.id.menu_add_post:
                        switchFragment(new PostFragment());
                        break;
                    case R.id.menu_perfil:
                        switchFragment(new PerfilFragment());
                        break;
                }
                return true;

            }
        });
    }

    private void switchFragment(Fragment f) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPage, f).commit();
    }

}