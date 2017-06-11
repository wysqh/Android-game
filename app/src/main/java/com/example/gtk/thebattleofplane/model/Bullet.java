package com.example.gtk.thebattleofplane.model;

/**
 * Created by gutia on 2017-06-09.
 */

public class Bullet {
    private int bullet_X;
    private int bullet_Y;
    private int bullet_X_Radius;
    private int bullet_Y_Radius;
    private int bullet_speed;
    private int bullet_type;
    private int bullet_range_capacity;

    public Bullet(int bullet_X, int bullet_Y, int bullet_X_Radius, int bullet_Y_Radius, int bullet_speed, int bullet_type,
                  int bullet_range_capacity) {
        this.bullet_X = bullet_X;
        this.bullet_Y = bullet_Y;
        this.bullet_X_Radius = bullet_X_Radius;
        this.bullet_Y_Radius = bullet_Y_Radius;
        this.bullet_speed = bullet_speed;
        this.bullet_type = bullet_type;
        this.bullet_range_capacity = bullet_range_capacity;
    }

    public void setBullet_X(int bullet_X) {
        this.bullet_X = bullet_X;
    }

    public void setBullet_Y(int bullet_Y) {
        this.bullet_Y = bullet_Y;
    }

    public int getBullet_X() {
        return bullet_X;
    }

    public int getBullet_Y() {
        return bullet_Y;
    }

    public int getBullet_X_Radius() {
        return bullet_X_Radius;
    }

    public int getBullet_Y_Radius() {
        return bullet_Y_Radius;
    }

    public int getBullet_speed() {
        return bullet_speed;
    }

    public int getBullet_type() {
        return bullet_type;
    }

    public int getBullet_range_capacity() {
        return bullet_range_capacity;
    }
}
