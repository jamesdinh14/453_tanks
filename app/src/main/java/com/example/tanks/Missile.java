package com.example.tanks;

import android.graphics.Bitmap;

import static com.example.tanks.Missile.Direction.*;

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
    };

	private static final long serialVersionUID = 1L;
    private float position_x, position_y;
    private Direction x_direction, y_direction;
    /**
     * velocity is is velocity of the traveling missile
     */
    private static final float VELOCITY = 25;
    /**
     * The size of the missile
     */
	private int size = 30;
    /*
     * Constructor: float x is x coordinate of missile float y is y coordinate of
     * missile
     *
     * @param location shows missile location
     */

	public Missile(float x, float y, float missile_dx, float missile_dy) {
		position_x = x;
        position_y = y;
        setMissileDirection(missile_dx, missile_dy);
	}
    /**
     * getter for location of x point of missile
     */
	public float getX() {
		return position_x;
	}
    /**
     * getter for location of y point of missile
     */
	public float getY() {
		return position_y;
	}
    /*
     * @param magnitude, calculates magnitude and direction of missile
     * @param location.x, location.y is x and y coordinate the missile will travel
     */
    protected void setMissileDirection(float x_direction, float y_direction) {
        this.x_direction = (x_direction < 0? NEGATIVE : POSITIVE);
        this.y_direction = (y_direction < 0? NEGATIVE : POSITIVE);
    }
    /*
     * @param magnitude, calculates magnitude and direction of missile
     * @param location .x, location.y is x and y coordinate the missile will travel
     */
	protected void moveMissile() {
        position_x += VELOCITY * x_direction.getValue();
        position_y += VELOCITY * y_direction.getValue();
	}

}