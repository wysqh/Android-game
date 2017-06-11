package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class Hit3SmallEnemy extends SmallEnemyPlane {
    public Hit3SmallEnemy(){
        super();
        state = SmallEnemyPlane.HIT3;
    }

    public Hit3SmallEnemy(Bitmap bitmap) {
        super();
        state = SmallEnemyPlane.HIT3;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
    }

    public Hit3SmallEnemy(Bitmap bitmap, int x, int y) {
        super();
        state = SmallEnemyPlane.HIT3;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
        plane_X = x;
        plane_Y = y;
    }

    public Hit3SmallEnemy(int x, int y, int rx, int ry) {
        plane_X = x;
        plane_Y = y;
        plane_X_Radius = rx;
        plane_Y_Radius = ry;
    }

    @Override
    public void performTask() {
        //Actions;
    }

    @Override
    public void changeState() {
        state = SmallEnemyPlane.CRASHED;
        enemyPlaneManager.setState(state);
    }
}
