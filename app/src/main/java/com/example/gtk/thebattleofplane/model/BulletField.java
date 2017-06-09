package com.example.gtk.thebattleofplane.model;

import android.util.Log;

/**
 * Created by gutia on 2017-06-09.
 */

public class BulletField {
    public int center_x;
    public int center_y;
    public int left;
    public int right;
    public int top;
    public int bottom;

    public BulletField(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.center_x = (left + right) / 2;
        this.center_y = (top + bottom) / 2;
    }

    public boolean inBounds(int x, int y) {
        Log.i("Points", left + "," + right + "," + top + ", " + bottom);
        return (x >= left && x <= right && y <= bottom && y >= top);
    }
}
