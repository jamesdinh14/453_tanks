package com.example.tanks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author @author Prem Muni, James Dinh, Deemal Patel
 *         This is the missile class used to create and move the missile
 */
public class Missile {

    public enum Direction {
        POSITIVE(1),
        NEGATIVE(-1);

        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    ;
    private static final long serialVersionUID = 1L;
    private float position_x, position_y;
    private Direction x_direction, y_direction;
    private static final float VELOCITY = 10;
    private Bitmap missile_image;

    /**
     * @param x            is x position coordinate of missile
     * @param y            is y position coordinate of missile
     * @param missile_dx   is the traveling position in the x
     * @param missile_dy   is the traveling position in the y
     * @param r            is the resource
     * @param missile_size is the size of our missile that the tanks fire
     */
    public Missile(float x, float y, float missile_dx, float missile_dy, Resources r, int missile_size) {
        position_x = x;
        position_y = y;
        setMissileDirection(missile_dx, missile_dy);
        Bitmap missile_bitmap = BitmapFactory.decodeResource(r, R.drawable.missile);
        missile_image = Bitmap.createScaledBitmap(missile_bitmap, missile_size, missile_size, true);
    }

    /**
     * @return getter for location of x point of missile
     */
    public float getX() {
        return position_x;
    }

    /**
     * @return getter for location of y point of missile
     */
    public float getY() {
        return position_y;
    }

    /**
     * @param x_direction - make missile travel in x direction
     * @param y_direction - make missile travel in y direction
     */
    protected void setMissileDirection(float x_direction, float y_direction) {
        this.x_direction = (x_direction < 0 ? Direction.NEGATIVE : Direction.POSITIVE);
        this.y_direction = (y_direction < 0 ? Direction.NEGATIVE : Direction.POSITIVE);
    }

    protected void moveMissile() {
        position_x += VELOCITY * x_direction.getValue();
        position_y += VELOCITY * y_direction.getValue();
    }

    /**
     * @return the missile everytime the method is call.
     * Method is used to draw the missile
     */
    public Bitmap getMissileImage() {
        return missile_image;
    }
}