package com.example.instagramclone.util;

import android.util.Log;

import androidx.annotation.NonNull;

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

    public static boolean updateUserName(String name){
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
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
