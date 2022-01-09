package com.example.instagramclone.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Utils {
    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean someEmptyFields(@NonNull String... fields){
        for (String field : fields ){
            if (field.isEmpty()){
                return true;
            }
        }
        return false;
    }
}
