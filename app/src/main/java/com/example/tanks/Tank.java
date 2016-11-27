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
    private static final float COR = 0;

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

    protected void updatePosition(float accel_x, float accel_y, float frame_time) {

        velocity_x += (accel_x * frame_time);
        velocity_y += (accel_y * frame_time);

        float distance_x = (velocity_x/2) * frame_time;
        float distance_y = (velocity_y/2) * frame_time;

        // Sensor readings are opposite to what we want
        position_x -= distance_x;
        position_y -= distance_y;
    }

	// Calculate displacement using acceleration
	protected void updatePosition(float acceleration_x, float acceleration_y,
								  float acceleration_z, long timestamp) {
		// Change in time
		float dt = (System.nanoTime() - timestamp) / 5000000000000.0f;
		velocity_x += -acceleration_x * dt;
		velocity_y += -acceleration_y * dt;

		position_x += velocity_x * dt;
		position_y += velocity_y * dt;
	}

    protected void resolveCollisionWithBounds(float horizontal_bound, float vertical_bound) {
        if (position_x > horizontal_bound) {
            position_x = (int)horizontal_bound;
            velocity_x = -velocity_x * COR;
        } else if (position_x < -horizontal_bound) {
            position_x = (int)-horizontal_bound;
            velocity_x = -velocity_x * COR;
        }

        if (position_y > vertical_bound) {
            position_y = (int)vertical_bound;
            velocity_y = -velocity_y * COR;
        } else if (position_y < -vertical_bound) {
            position_y = (int)-vertical_bound;
            velocity_y = -velocity_y * COR;
        }
    }
}