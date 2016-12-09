package com.example.tanks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * @author Prem Muni, James Dinh, Deemal Patel
 *         Tank class used to create the tanks attribute such as health,
 *         missile launching capability etc
 */
public class Tank {

    private float position_x, position_y;
    /**
     * User tank's health.
     */
    private int hp = 3;
    /**
     * Checks the bounds of the map
     */
    private Rect perimeter;
    /**
     * used for creating and moving the missile
     */
    private ArrayList<Missile> missileList = new ArrayList<Missile>();
    /**
     * perimeter size
     */
    private int size = 20;
    /**
     * Used to determine velocity in both x and y direction
     */
    private float velocity_x, velocity_y;

    public Tank(float position_x, float position_y) {
        this.position_x = position_x;
        this.position_y = position_y;
    }

    /**
     * @return hp of player's own tank
     */
    public int getHp() {
        return hp;
    }

    /**
     * @return location of y coordinate
     */
    public float getY() {
        return position_y;
    }

    /**
     * @return location of x coordinate
     */
    public float getX() {
        return position_x;
    }

    /**
     * Method use for firing the missile
     *
     * @param x            is x position coordinate of missile
     * @param y            is y position coordinate of missile
     * @param missile_dx   is the traveling position in the x
     * @param missile_dy   is the traveling position in the y
     * @param r            is the resource
     * @param missile_size is the size of our missile that the tanks fire
     */
    protected void fireMissile(float x, float y, float missile_dx, float missile_dy, Resources r, int missile_size) {
        missileList.add(new Missile(x, y, missile_dx, missile_dy, r, missile_size));
    }

    /**
     * @return the missiles being used
     */
    public ArrayList<Missile> getMissileList() {
        return missileList;
    }

    /**
     * checks if tank has been hit by missile and reduce that tanks health
     */
    public void missileHit() {
        if (hp > 0) {
            hp--;
        }
    }

    /**
     * @return perimeter - checks the parameter
     */
    public Rect getBorder() {
        return perimeter = new Rect((int) getX(), (int) getY(), size, size);
    }

    /**
     * Checks if tank has been hit by missile
     *
     * @param rectangle - rectangle object
     * @param t         - tank object
     * @return
     */
    public boolean hitByMissile(ArrayList<Missile> rectangle, Tank t) {
        for (int i = 0; i < rectangle.size(); i++) {
            if (t.getBorder().contains((int) rectangle.get(i).getX(), (int) rectangle.get(i).getY())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the tank using the accelerometer
     *
     * @param accel_x    - accelerometer moves tank in x direction
     * @param accel_y-   accelerometer moves tank in y direction
     * @param frame_time - the time per frame it takes to move the tank
     */
    protected void updatePosition(float accel_x, float accel_y, float frame_time) {

        // Calculate velocity using kinematics
        velocity_x += (accel_x * frame_time);
        velocity_y += (accel_y * frame_time);

        // Calculate distance traveled using kinematics
        float distance_x = (velocity_x / 2) * frame_time;
        float distance_y = (velocity_y / 2) * frame_time;

        // Sensor readings are opposite to what we want
        position_x -= distance_x;
        position_y -= distance_y;
    }

    /**
     * Tanks don't bounce off of boundaries, so speed = 0
     *
     * @param horizontal_bound - checks the horizantal bound
     * @param vertical_bound   - checks the vertical bounds
     */
    protected void resolveCollisionWithBounds(float horizontal_bound, float vertical_bound) {
        if (position_x > horizontal_bound) {
            position_x = (int) horizontal_bound;
            velocity_x = 0;
        } else if (position_x < -horizontal_bound) {
            position_x = (int) -horizontal_bound;
            velocity_x = 0;
        }

        if (position_y > vertical_bound) {
            position_y = (int) vertical_bound;
            velocity_y = 0;
        } else if (position_y < -vertical_bound) {
            position_y = (int) -vertical_bound;
            velocity_y = 0;
        }
    }
}