package com.example.gtk.thebattleofplane.model;

import android.graphics.Bitmap;

/**
 * Created by gutia on 2017-06-11.
 */

public class SmallEnemyPlaneManager {
    private SmallEnemyPlane enemyPlane;
    private InitialSmallEnemy initialSmallEnemy;
    private HitSmallEnemy hitSmallEnemy;
    private Hit2SmallEnemy hit2SmallEnemy;
    private Hit3SmallEnemy hit3SmallEnemy;
    private CrashedSmallEnemy crashedSmallEnemy;

    private String state;

    public SmallEnemyPlaneManager(SmallEnemyPlane smallEnemyPlane){
        enemyPlane = smallEnemyPlane;
        initialSmallEnemy = new InitialSmallEnemy(smallEnemyPlane.getPlane_X(), smallEnemyPlane.getPlane_Y(),
                smallEnemyPlane.getPlane_X_Radius(), smallEnemyPlane.getPlane_Y_Radius());
        hitSmallEnemy = new HitSmallEnemy(smallEnemyPlane.getPlane_X(), smallEnemyPlane.getPlane_Y(),
                smallEnemyPlane.getPlane_X_Radius(), smallEnemyPlane.getPlane_Y_Radius());
        hit2SmallEnemy = new Hit2SmallEnemy(smallEnemyPlane.getPlane_X(), smallEnemyPlane.getPlane_Y(),
                smallEnemyPlane.getPlane_X_Radius(), smallEnemyPlane.getPlane_Y_Radius());
        hit3SmallEnemy = new Hit3SmallEnemy(smallEnemyPlane.getPlane_X(), smallEnemyPlane.getPlane_Y(),
                smallEnemyPlane.getPlane_X_Radius(), smallEnemyPlane.getPlane_Y_Radius());
        crashedSmallEnemy = new CrashedSmallEnemy(smallEnemyPlane.getPlane_X(), smallEnemyPlane.getPlane_Y(),
                smallEnemyPlane.getPlane_X_Radius(), smallEnemyPlane.getPlane_Y_Radius());
    }

    public SmallEnemyPlane getEnemyPlane() {
        return enemyPlane;
    }

    public void setEnemyPlane(SmallEnemyPlane enemyPlane) {
        this.enemyPlane = enemyPlane;
    }

    public String getSmallPlaneState() {
        return enemyPlane.getCurrentState();
    }

    public Bitmap getBitmap(){
        return enemyPlane.getEnemyBitmap();
    }

    public void doAction(){
        enemyPlane = setUpStateObj();
        enemyPlane.performTask();
    }

    public void setState(String st) {
        state = st;
    }

    public SmallEnemyPlane setUpStateObj() {
        if (state.equals(SmallEnemyPlane.INITIAL)){
            enemyPlane.setUpEnemyPlaneManager(this);
        } else if (state.equals(SmallEnemyPlane.HIT)) {
            hitSmallEnemy.updatePosition(enemyPlane);
            hitSmallEnemy.setState(state);
            enemyPlane = hitSmallEnemy;
            enemyPlane.setUpEnemyPlaneManager(this);
            enemyPlane.setDead(true);
        } else if (state.equals(SmallEnemyPlane.HIT2)) {
            hit2SmallEnemy.updatePosition(hitSmallEnemy);
            hit2SmallEnemy.setState(state);
            enemyPlane = hit2SmallEnemy;
            enemyPlane.setUpEnemyPlaneManager(this);
            enemyPlane.setDead(true);
        } else if (state.equals(SmallEnemyPlane.HIT3)) {
            hit3SmallEnemy.updatePosition(hit2SmallEnemy);
            hit3SmallEnemy.setState(state);
            enemyPlane = hit3SmallEnemy;
            enemyPlane.setUpEnemyPlaneManager(this);
            enemyPlane.setDead(true);
        }else if (state.equals(SmallEnemyPlane.CRASHED)) {
            crashedSmallEnemy.updatePosition(hit3SmallEnemy);
            crashedSmallEnemy.setState(state);
            enemyPlane = crashedSmallEnemy;
            enemyPlane.setUpEnemyPlaneManager(this);
            enemyPlane.setDead(true);
        }
        return enemyPlane;
    }
}
