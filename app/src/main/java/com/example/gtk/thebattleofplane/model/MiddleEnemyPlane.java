package com.example.gtk.thebattleofplane.model;

/**
 * Created by gutia on 2017-06-11.
 */

abstract public class MiddleEnemyPlane extends EnemyPlane {
    public MiddleEnemyPlane(){
        super();
        plane_speed = Plane.MIDDLE_SPEED;
    }
}
