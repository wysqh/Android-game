package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class HitSmallEnemy extends SmallEnemyPlane {
    public HitSmallEnemy(){
        state = SmallEnemyPlane.HIT;
    }

    public HitSmallEnemy(Bitmap bitmap) {
        super();
        state = SmallEnemyPlane.HIT;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
    }

    public HitSmallEnemy(Bitmap bitmap, int x, int y) {
        super();
        state = SmallEnemyPlane.HIT;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
        plane_X = x;
        plane_Y = y;
    }

    public HitSmallEnemy(int x, int y, int rx, int ry) {
        plane_X = x;
        plane_Y = y;
        plane_X_Radius = rx;
        plane_Y_Radius = ry;
    }

    @Override
    public void performTask() {
        //do Action
    }

    @Override
    public void changeState() {
        state = SmallEnemyPlane.HIT2;
        enemyPlaneManager.setState(state);
    }
}
