package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

abstract public class EnemyPlane extends Plane {
    int enemyPlaneSpeed;    //敌机速度

    public void setEnemyPlaneSpeed(int enemyPlaneSpeed) {
        this.enemyPlaneSpeed = enemyPlaneSpeed;
    }

    public int getEnemyPlaneSpeed() {
        return enemyPlaneSpeed;
    }
}
