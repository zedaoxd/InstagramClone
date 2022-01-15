package com.example.instagramclone.util;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.instagramclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.Serializable;

public class UserFirebase implements Serializable {

    public static FirebaseUser getCurrentUserFirebase(){
        FirebaseAuth user = FirebaseUtils.getFirebaseAuth();
        return user.getCurrentUser();
    }

    public static void updateUserName(String name){

        try {

            FirebaseUser user = getCurrentUserFirebase();
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateUserPhoto(Uri url){

        try {

            FirebaseUser user = getCurrentUserFirebase();
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static User getDataCurrentUser(){
        FirebaseUser userFirebase = getCurrentUserFirebase();

        User user = new User();
        user.setEmail(userFirebase.getEmail());
        user.setName(userFirebase.getDisplayName());
        user.setId(userFirebase.getUid());

        if (userFirebase.getPhotoUrl() == null){
            user.setPhotoPath("");
        } else {
            user.setPhotoPath(userFirebase.getPhotoUrl().toString());
        }

        return user;
    }

    public static String getUserId() {
        return getCurrentUserFirebase().getUid();
    }
}
