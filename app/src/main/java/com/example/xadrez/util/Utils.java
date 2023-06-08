package com.example.xadrez.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.example.xadrez.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {

    private static Point size;

    public static FirebaseAuth confirmAuth(Activity activity) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        return auth;
    }

    public static int getWidth() {
        return size.x;
    }

    public static void setSize(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        Utils.size = new Point();
        display.getSize(size);
    }

}
