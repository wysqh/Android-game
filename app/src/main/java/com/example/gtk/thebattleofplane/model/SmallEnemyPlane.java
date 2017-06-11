package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

abstract public class SmallEnemyPlane extends EnemyPlane {
    public static final String INITIAL = "initial";
    public static final String HIT = "hit";
    public static final String HIT2 = "hit2";
    public static final String HIT3 = "hit3";
    public static final String CRASHED = "crashed";
    public static final String DISAPPEAR = "disappear";
    public static final Integer GRADE = 100;

    protected String state = null;
    protected Bitmap enemyBitmap;

    protected SmallEnemyPlaneManager enemyPlaneManager;

    public SmallEnemyPlane() {
        super();
        plane_speed = Plane.QUICK_SPEED;
    }

    public Bitmap getEnemyBitmap() {
        return enemyBitmap;
    }

    public void setEnemyBitmap(Bitmap enemyBitmap) {
        this.enemyBitmap = enemyBitmap;
    }

    public String getCurrentState(){
        return state;
    }

    public void setUpEnemyPlaneManager(SmallEnemyPlaneManager epm){
        enemyPlaneManager = epm;
    }

    public SmallEnemyPlaneManager getEnemyPlaneManager() {
        return enemyPlaneManager;
    }

    public void setEnemyPlaneManager(SmallEnemyPlaneManager enemyPlaneManager) {
        this.enemyPlaneManager = enemyPlaneManager;
    }

    public void updatePosition(int x, int y, int rx, int ry){
        plane_X_Radius = rx;
        plane_Y_Radius = ry;
        plane_X = x;
        plane_Y = y;
    }

    public void updatePosition(SmallEnemyPlane smallEnemyPlane){
        plane_X_Radius = smallEnemyPlane.getPlane_X_Radius();
        plane_Y_Radius = smallEnemyPlane.getPlane_Y_Radius();
        plane_X = smallEnemyPlane.getPlane_X();
        plane_Y = smallEnemyPlane.getPlane_Y();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public abstract void performTask();
    public abstract void changeState();
}
