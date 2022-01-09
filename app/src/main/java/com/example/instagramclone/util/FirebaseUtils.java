package com.example.instagramclone.util;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {

    private static FirebaseAuth auth;
    private static StorageReference storage;
    private static DatabaseReference reference;

    @NonNull
    public static FirebaseAuth getFirebaseAuth(){
        if (auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }

    @NonNull
    public static DatabaseReference getDatabaseReference(){
        if (reference == null) reference = FirebaseDatabase.getInstance().getReference();
        return reference;
    }

    @NonNull
    public static StorageReference getFirebaseStorage(){
        if (storage == null) storage = FirebaseStorage.getInstance().getReference();
        return storage;
    }
}
