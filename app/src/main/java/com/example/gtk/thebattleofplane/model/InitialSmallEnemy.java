package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class InitialSmallEnemy extends SmallEnemyPlane {
    public InitialSmallEnemy(){
        super();
        state = SmallEnemyPlane.INITIAL;
    }

    public InitialSmallEnemy(Bitmap bitmap) {
        super();
        state = SmallEnemyPlane.INITIAL;
        enemyBitmap = bitmap;
        plane_X_Radius = bitmap.getWidth()/2;
        plane_Y_Radius = bitmap.getHeight()/2;
    }

    public InitialSmallEnemy(int x, int y, int rx, int ry) {
        plane_X = x;
        plane_Y = y;
        plane_X_Radius = rx;
        plane_Y_Radius = ry;
    }

    @Override
    public void performTask() {
        //do Actions;
        changeState();
    }

    @Override
    public void changeState() {
        state = SmallEnemyPlane.HIT;
        enemyPlaneManager.setState(state);
    }
}
