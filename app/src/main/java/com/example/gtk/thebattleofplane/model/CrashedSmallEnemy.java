package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class CrashedSmallEnemy extends SmallEnemyPlane {
    public CrashedSmallEnemy() {
        super();
        state = SmallEnemyPlane.CRASHED;
    }

    public CrashedSmallEnemy(Bitmap bitmap) {
        super();
        state = SmallEnemyPlane.CRASHED;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
    }

    public CrashedSmallEnemy(Bitmap bitmap, int x, int y) {
        super();
        state = SmallEnemyPlane.CRASHED;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
        plane_X = x;
        plane_Y = y;
    }

    public CrashedSmallEnemy(int x, int y, int rx, int ry) {
        state = SmallEnemyPlane.CRASHED;
        plane_X = x;
        plane_Y = y;
        plane_X_Radius = rx;
        plane_Y_Radius = ry;
    }

    @Override
    public void performTask() {
        //Actions
    }

    @Override
    public void changeState() {
        state = SmallEnemyPlane.DISAPPEAR;
        enemyPlaneManager.setState(state);
    }
}
