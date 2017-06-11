package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class Hit2SmallEnemy extends SmallEnemyPlane {
    public Hit2SmallEnemy(){
        super();
        state = SmallEnemyPlane.HIT2;
    }

    public Hit2SmallEnemy(Bitmap bitmap) {
        super();
        state = SmallEnemyPlane.HIT2;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
    }

    public Hit2SmallEnemy(Bitmap bitmap, int x, int y) {
        super();
        state = SmallEnemyPlane.HIT2;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
        plane_X = x;
        plane_Y = y;
    }

    public Hit2SmallEnemy(int x, int y, int rx, int ry) {
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
        state = SmallEnemyPlane.HIT3;
        enemyPlaneManager.setState(state);
    }
}
