package com.example.tanks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TankView tankView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tankView = new TankView(this);

        setContentView(tankView);
    }

    @Override
    public void onResume() {
        super.onResume();
        tankView.resumeGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        tankView.pauseGame();
    }
}
