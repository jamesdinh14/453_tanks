package com.example.tanks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by James on 11/16/2016.
 */

public class TankView extends View implements SensorEventListener, View.OnTouchListener {

    private Bitmap tank, terrain, missile_image;
    private Tank tank_player;
    private int tank_size, missile_size;
    private ArrayList<Tank> tanks;
    private static final float TANK_SIZE_SCREEN_MODIFIER = 0.10f;
    private static final float MISSILE_SIZE_SCREEN_MODIFIER = 0.33f;
    private float x_origin, y_origin, horizontal_bound, vertical_bound;
    private float accel_x, accel_y, accel_z;
    private long timestamp;
    private float missile_direction_x, missile_direction_y;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Display display;
    private float frame_time = 0.25f; //Use this to control how much acceleration is used by tank
                                      // Keep the accelerometer values low to keep tank slow

    public TankView(Context context) {
        super(context);

        // Obtain width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        display.getMetrics(displayMetrics);
        int display_width = displayMetrics.widthPixels;
        int display_height = displayMetrics.heightPixels;

        tanks = new ArrayList<>();
        tank_player = new Tank(x_origin, y_origin);
        tanks.add(tank_player);

        // Scale tank size based on screen size
        tank_size = (int)(average(display_width, display_height) * TANK_SIZE_SCREEN_MODIFIER);
        missile_size = (int)(tank_size * MISSILE_SIZE_SCREEN_MODIFIER);

        Bitmap tank_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tank);
        tank = Bitmap.createScaledBitmap(tank_bitmap, tank_size, tank_size, true);

        Bitmap missile_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.missile);
        missile_image = Bitmap.createScaledBitmap(missile_bitmap, missile_size, missile_size, true);

        Bitmap desert_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.desert);
        terrain = Bitmap.createScaledBitmap(desert_bitmap, display_width, display_height, true);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        x_origin = width / 2.0f;
        y_origin = height / 2.0f;

        horizontal_bound = (width - tank_size) / 2.0f;
        vertical_bound = (height - tank_size) / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(terrain, 0, 0, null);

        tank_player.updatePosition(accel_x, accel_y, frame_time);
        tank_player.resolveCollisionWithBounds(horizontal_bound, vertical_bound);

        // Draw all missiles
        for (int i = 0; i < tanks.size(); i++) {
            for (int j = 0; j < tanks.get(i).getMissileList().size(); j++) {
                Missile missile = tanks.get(i).getMissileList().get(j);
                missile.moveMissile();
                canvas.drawBitmap(missile_image, (x_origin - missile_size/2) + missile.getX(),
                        (y_origin - missile_size/2) - missile.getY(), null);
            }
        }

        canvas.drawBitmap(tank, (x_origin - tank_size/2) + tank_player.getX(),
                (y_origin - tank_size/2) - tank_player.getY(), null);

        invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if (display.getRotation() == Surface.ROTATION_0) {
                accel_x = event.values[0];
                accel_y = event.values[1];
            }

            if (display.getRotation() == Surface.ROTATION_90) {
                accel_x = -event.values[1];
                accel_y = event.values[0];
            }

            accel_z = event.values[2];
            timestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        missile_direction_x = event.getX();
        missile_direction_y = event.getY();
        tank_player.fireMissile(tank_player.getX(), tank_player.getY(),
                missile_direction_x, missile_direction_y);
        return true;
    }

    public void resumeGame() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pauseGame() {
        sensorManager.unregisterListener(this);
    }

    // Get average of screen width and height
    private float average(int a, int b) {
        return (a + b) / 2.0f;
    }
}
