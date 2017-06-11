package com.example.gtk.thebattleofplane.model;

/**
 * Created by gutia on 2017-06-11.
 */

abstract public class LargeEnemyPlane extends EnemyPlane {
    public LargeEnemyPlane(){
        super();
        plane_speed = Plane.SLOW_SPEED;
    }
}
