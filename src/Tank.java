package com.example.tanks;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class Tank {

	private float position_x, position_y;
	private int hp = 3;
	private Rect perimeter;
	private ArrayList<Missile> missileList = new ArrayList<Missile>();
	private int size = 20;
    private float velocity_x, velocity_y;

	public Tank(float position_x, float position_y) {
        this.position_x = position_x;
        this.position_y = position_y;
	}

	public int getHp() {
		return hp;
	}

	public float getY() {
		return position_y;
	}

	public float getX() {
		return position_x;
	}

	protected void fireMissile(float x, float y, float missile_dx, float missile_dy) {
		missileList.add(new Missile(x, y, missile_dx, missile_dy));
	}

	public ArrayList<Missile> getMissileList() {
        return missileList;
    }

	public void missileHit() {
		if (hp > 0) {
			hp--;
		}
	}

	public Rect getBorder() {
		return perimeter = new Rect((int)getX(), (int)getY(), size, size);
	}

	public boolean hitByMissile(ArrayList<Missile> rectangle, Tank t) {
		for (int i = 0; i < rectangle.size(); i++) {
			if (t.getBorder().contains((int)rectangle.get(i).getX(), (int)rectangle.get(i).getY())) {
				return true;
			}		
		}
		return false;
	}

	// Moves the tank using the accelerometer
    protected void updatePosition(float accel_x, float accel_y, float frame_time) {

        // Calculate velocity using kinematics
        velocity_x += (accel_x * frame_time);
        velocity_y += (accel_y * frame_time);

        // Calculate distance traveled using kinematics
        float distance_x = (velocity_x/2) * frame_time;
        float distance_y = (velocity_y/2) * frame_time;

        // Sensor readings are opposite to what we want
        position_x -= distance_x;
        position_y -= distance_y;
    }

    // Tanks don't bounce off of boundaries, so speed = 0
    protected void resolveCollisionWithBounds(float horizontal_bound, float vertical_bound) {
        if (position_x > horizontal_bound) {
            position_x = (int)horizontal_bound;
            velocity_x = 0;
        } else if (position_x < -horizontal_bound) {
            position_x = (int)-horizontal_bound;
            velocity_x = 0;
        }

        if (position_y > vertical_bound) {
            position_y = (int)vertical_bound;
            velocity_y = 0;
        } else if (position_y < -vertical_bound) {
            position_y = (int)-vertical_bound;
            velocity_y = 0;
        }
    }
}