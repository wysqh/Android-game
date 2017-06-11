package com.example.gtk.thebattleofplane.model;

/**
 * Created by gutia on 2017-06-11.
 */

public class PlaneFactory {
    public static final int HERO_PLANE = 0;
    public static final int SMALL_ENEMY_PLANE = 1;
//    public static final int MIDDLE_ENEMY_PLANE = 2;
//    public static final int LARGE_ENEMY_PLANE = 3;

    public static Plane createPlane(int PLANE_TYPE){
        Plane plane = null;

        if (PLANE_TYPE == HERO_PLANE) {
            plane = new HeroPlane();
        }else if (PLANE_TYPE == SMALL_ENEMY_PLANE) {
            plane = new InitialSmallEnemy();
        }
//        else if (PLANE_TYPE == MIDDLE_ENEMY_PLANE) {
//            plane = new MiddleEnemyPlane();
//        }else if (PLANE_TYPE == LARGE_ENEMY_PLANE) {
//            plane = new LargeEnemyPlane();
//        }

        return plane;
    }
}
