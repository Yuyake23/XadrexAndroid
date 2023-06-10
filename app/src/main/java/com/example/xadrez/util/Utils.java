package com.example.xadrez.util;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

    private static Point size;

    public static int getWidth() {
        return size.x;
    }

    public static void setSize(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        Utils.size = new Point();
        display.getSize(size);
    }

}
