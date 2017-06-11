package com.example.gtk.thebattleofplane.model;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by gutia on 2017-06-02.
 */

abstract public class Plane {
    public static final int SLOW_SPEED = 15;
    public static final int MIDDLE_SPEED = 20;
    public static final int QUICK_SPEED = 25;

    int plane_X;
    int plane_Y;
    int plane_X_Radius;
    int plane_Y_Radius;
    int plane_speed;
    boolean isDead = false;

    List<Bullet> bullets;

    public Plane(){

    }

    public Plane(int plane_X, int plane_Y, int plane_X_Radius, int plane_Y_Radius) {
        this.plane_X = plane_X;
        this.plane_Y = plane_Y;
        this.plane_X_Radius = plane_X_Radius;
        this.plane_Y_Radius = plane_Y_Radius;
    }

    public boolean inBounds(int x, int y) {
        return (x <= plane_X + plane_X_Radius &&
            x >= plane_X - plane_X_Radius &&
            y >= plane_Y - plane_Y_Radius &&
            y <= plane_Y + plane_Y_Radius);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public int getPlane_X() {
        return plane_X;
    }

    public void setPlane_X(int plane_X) {
        this.plane_X = plane_X;
    }

    public int getPlane_Y() {
        return plane_Y;
    }

    public void setPlane_Y(int plane_Y) {
        this.plane_Y = plane_Y;
    }

    public int getPlane_X_Radius() {
        return plane_X_Radius;
    }

    public void setPlane_X_Radius(int plane_X_Radius) {
        this.plane_X_Radius = plane_X_Radius;
    }

    public int getPlane_Y_Radius() {
        return plane_Y_Radius;
    }

    public void setPlane_Y_Radius(int plane_Y_Radius) {
        this.plane_Y_Radius = plane_Y_Radius;
    }

    public int getPlane_speed() {
        return plane_speed;
    }

    public void setPlane_speed(int plane_speed) {
        this.plane_speed = plane_speed;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
