package com.example.instagramclone.util;

import android.content.Context;
import android.widget.Toast;

public class AlertUtil {
    public static void toastMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
