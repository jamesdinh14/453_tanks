package com.example.tanks;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * @author @author Deemal Patel, Prem Muni, James Dinh,
 *         This is the tank view class. This class draws all the components
 *         of the game and has a lot of the logic on how the game works.
 */

public class TankView extends View implements SensorEventListener/*, View.OnTouchListener*/ {
    /*
    Bitmap image object of every tank
     */
    private Bitmap tank, tank1, tank2, tank3, terrain;
    /*
    The user tanks
    */
    private Tank tank_player;
    /*
    The enemy tanks
    */
    private Tank enemy_player1, enemy_player2, enemy_player3;
    /*
    The tanks size, missile size, and health
    */
    private int tank_size, missile_size, HP1, HP2, HP3, PHP;
    /*
    Array list that store the tank objects
    */
    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    /*
    Tank size on screen
    */
    private static final float TANK_SIZE_SCREEN_MODIFIER = 0.10f;
    /*
    Missile screen size
    */
    private static final float MISSILE_SIZE_SCREEN_MODIFIER = 0.33f;
    /*
    Boundary for tanks and missiles to travel
    */
    private float x_origin, y_origin, horizontal_bound, vertical_bound;
    /*
    Acceraltion for x and y direction
    */
    private float accel_x, accel_y;
    /*
   Missile travel in x and y direction
    */
    private float missile_direction_x, missile_direction_y;
    /*
    Use to create tanks
    */
    private int tankNumbers;
    /*
    Sensors object to manage accelerometer
    */
    private SensorManager sensorManager;
    /*
    Accelerometer to move tanks
    */
    private Sensor accelerometer;
    /*
    Display for displaying objects
    */
    private Display display;
    /*
    Use this to control how much acceleration is used by tank
    Keep the accelerometer values low to keep tank slow
    */
    private float frame_time = 0.25f;

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

        //Creating our player tank
        tanks = new ArrayList<>();
        tank_player = new Tank(x_origin, y_origin);
        tanks.add(tank_player);

        //Creating our enemy tanks
        enemy_player1 = new Tank(x_origin - 200, y_origin + 300);
        enemy_player2 = new Tank(x_origin + 105, y_origin + 110);
        enemy_player3 = new Tank(x_origin + 100, y_origin + 120);


        // Scale tank size based on screen size
        tank_size = (int) (average(display_width, display_height) * TANK_SIZE_SCREEN_MODIFIER);
        missile_size = (int) (tank_size * MISSILE_SIZE_SCREEN_MODIFIER);

        //Bitmaps for our tanks
        Bitmap tank_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tank);
        Bitmap uTank = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        tank = Bitmap.createScaledBitmap(uTank, tank_size, tank_size, true);
        tank1 = Bitmap.createScaledBitmap(tank_bitmap, tank_size, tank_size, true);
        tank2 = Bitmap.createScaledBitmap(tank_bitmap, tank_size, tank_size, true);
        tank3 = Bitmap.createScaledBitmap(tank_bitmap, tank_size, tank_size, true);

        //Bitmap for the background
        Bitmap desert_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.desert);
        terrain = Bitmap.createScaledBitmap(desert_bitmap, display_width, display_height, true);

        //Sensors that we used
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    //When selecting difficulty we select how many tanks will be present for us to battle

    /**
     * @param hardness - takes the Level enter and create appropriate number of enemy tanks
     */
    public void createTanks(int hardness) {
        if (hardness >= 1) {
            enemy_player1 = new Tank(x_origin - 600, y_origin - 600);
            tanks.add(enemy_player1);
            HP1 = 3;
        }
        if (hardness >= 2) {
            enemy_player2 = new Tank(x_origin + 60, y_origin + 60);
            tanks.add(enemy_player2);
            HP1 = 4;
            HP2 = 4;
        }
        if (hardness == 3) {
            enemy_player3 = new Tank(x_origin - 300, y_origin + 90);
            tanks.add(enemy_player3);
            HP1 = 5;
            HP2 = 5;
            HP3 = 5;
        }
        tankNumbers = hardness;
        PHP = 5000;
    }

    /**
     * Sensor manager method to keep game running
     */
    public void resumeGame() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Sensor manager method to check for pauses
     */
    public void pauseGame() {
        sensorManager.unregisterListener(this);
    }

    /**
     * @param a -width
     * @param b -screen
     * @return - Returns the average of screen width and height
     */
    private float average(int a, int b) {
        return (a + b) / 2.0f;
    }

    /**
     * Updates the tank as it moves
     *
     * @param width     - gets updated width
     * @param height    - gets updated height
     * @param oldWidth  - get the old width
     * @param oldHeight - get the old height
     */
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        x_origin = width / 2.0f;
        y_origin = height / 2.0f;

        horizontal_bound = (width - tank_size) / 2.0f;
        vertical_bound = (height - tank_size) / 2.0f;
    }

    int x = 0;

    /**
     * @param canvas - used to draw the tank, map, and missiles as they move
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(terrain, 0, 0, null);

        canvas.drawBitmap(tank, (x_origin - tank_size / 2) + tank_player.getX(),
                (y_origin - tank_size / 2) - tank_player.getY(), null);
        if (HP1 != 0) {
            canvas.drawBitmap(tank1, (x_origin - tank_size / 2) + enemy_player1.getX(),
                    (y_origin - tank_size / 2) - enemy_player1.getY(), null);
        }

        if (tankNumbers >= 2) {
            if (HP2 != 0) {
                canvas.drawBitmap(tank2, (x_origin - tank_size / 2) + enemy_player2.getX(),
                        (y_origin - tank_size / 2) - enemy_player2.getY(), null);
            }
        }
        if (tankNumbers >= 3) {
            if (HP3 != 0) {
                canvas.drawBitmap(tank3, (x_origin - tank_size / 2) + enemy_player3.getX(),
                        (y_origin - tank_size / 2) - enemy_player3.getY(), null);
            }
        }


        //update our enemy and user positions when using sensors
        tank_player.updatePosition(accel_x, accel_y, frame_time);
        tank_player.resolveCollisionWithBounds(horizontal_bound, vertical_bound);

        enemy_player1.updatePosition(-accel_x, -accel_y, frame_time);
        enemy_player1.resolveCollisionWithBounds(horizontal_bound, vertical_bound);

        //Only creates other tanks if difficulty is set higher
        if (tankNumbers >= 2) {
            enemy_player2.updatePosition(-accel_x, accel_y, frame_time);
            enemy_player2.resolveCollisionWithBounds(horizontal_bound, vertical_bound);
        }
        if (tankNumbers >= 3) {
            enemy_player3.updatePosition(accel_x, -accel_y, frame_time);
            enemy_player3.resolveCollisionWithBounds(horizontal_bound, vertical_bound);
        }

        // Draw all missiles
        for (int i = 0; i < tanks.size(); i++) {
            for (int j = 0; j < tanks.get(i).getMissileList().size(); j++) {
                Missile missile = tanks.get(i).getMissileList().get(j);
                missile.moveMissile();
                canvas.drawBitmap(missile.getMissileImage(), (x_origin - missile_size / 2) + missile.getX(),
                        (y_origin - missile_size / 2) - missile.getY(), null);
                if (tank_player.hitByMissile(tanks.get(i).getMissileList(), enemy_player1) == true) //If Player gets hit
                {
                    PHP--; //Player loses health
                    if (PHP == 0) //Player runs out of health
                    {
                        Toast.makeText(this.getContext(), "YOU LOSE!!!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this.getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }
                }
                if (enemy_player1.hitByMissile(tanks.get(i).getMissileList(), enemy_player1) == true)//Similar to player execept for enemies
                {
                    HP1--;
                    if (HP1 == 0 && tankNumbers == 1) {
                        Toast.makeText(this.getContext(), "You Win", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this.getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }

                }
                if (enemy_player2.hitByMissile(tanks.get(i).getMissileList(), enemy_player2) == true) {
                    HP2--;
                    if (HP1 == 0) {
                        tank1.setPixel(0, 0, 0);
                    }
                    if (HP2 == 0) {
                        tank2.setPixel(0, 0, 0);
                    }
                    if (HP2 == 0 && HP1 == 0 && tankNumbers == 2) {
                        Toast.makeText(this.getContext(), "You Win", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this.getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }
                }
                if (enemy_player3.hitByMissile(tanks.get(i).getMissileList(), enemy_player3) == true) {
                    HP3--;
                    if (HP3 == 0) {
                        tank3.setPixel(0, 0, 0);
                    }
                    if (HP2 == 0 && HP1 == 0 && HP3 == 0) {
                        Toast.makeText(this.getContext(), "You Win!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this.getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }
                }
            }
        }


        invalidate();
    }

    /**
     * Allows the tanks to move
     *
     * @param event - sensor event object
     */
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
        }
    }

    /**
     * @param sensor   - sensor object
     * @param accuracy - accuracy object
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Allows the user to shoot missiles
     * Give ai knowledge to the enemy tanks
     *
     * @param event - event object
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Shift the event event origin (0,0) from the top-left to somewhere close to
            // the middle of the screen
            missile_direction_x = event.getX() - x_origin - tank_player.getX();
            missile_direction_y = -(event.getY() - y_origin) - tank_player.getY();
            tank_player.fireMissile(tank_player.getX(), tank_player.getY(),
                    missile_direction_x, missile_direction_y, getResources(), missile_size);

            /*Enemy firing capability*/
            missile_direction_x = -event.getX() - x_origin - enemy_player1.getX();
            missile_direction_y = -(event.getY() - y_origin) - enemy_player1.getY();
            enemy_player1.fireMissile(enemy_player1.getX(), enemy_player1.getY(),
                    missile_direction_x, missile_direction_y, getResources(), missile_size);

            if (tankNumbers >= 2) {
                missile_direction_x = event.getX() - x_origin - enemy_player2.getX();
                missile_direction_y = -(event.getY() - y_origin) - enemy_player2.getY();
                enemy_player2.fireMissile(enemy_player2.getX(), enemy_player2.getY(),
                        missile_direction_x, missile_direction_y, getResources(), missile_size);
            }
            if (tankNumbers >= 3) {
                missile_direction_x = event.getX() - x_origin - enemy_player3.getX();
                missile_direction_y = -(event.getY() - y_origin) - enemy_player3.getY();
                enemy_player3.fireMissile(enemy_player3.getX(), enemy_player3.getY(),
                        missile_direction_x, missile_direction_y, getResources(), missile_size);
            }
            return true;
        }
        return false;
    }

}