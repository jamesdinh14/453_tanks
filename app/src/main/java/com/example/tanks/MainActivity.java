package com.example.tanks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author @author  Prem Muni
 * The main activity control the enter game play.
 * User will select the level they decide to play,
 * and will use the sensors to move their tank
 * and tap the screen to shoot opponent tanks!
 */
public class MainActivity extends AppCompatActivity {

    private TankView tankView;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_main);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_dialog1);
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display 1 enemy tank
                tankView.createTanks(1);
                dialog.dismiss();
            }
        });
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn_dialog2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display 2 enemy tank
                tankView.createTanks(2);
                dialog.dismiss();
            }
        });
        Button dialogButton3 = (Button) dialog.findViewById(R.id.btn_dialog3);
        dialogButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display 3 enemy tank
                tankView.createTanks(3);
                dialog.dismiss();
            }
        });
        dialog.show();
        tankView = new TankView(this);
        setContentView(tankView);
    }

    /**
     * Keeps the gaming running
     */
    @Override
    public void onResume() {
        super.onResume();
        tankView.resumeGame();
    }
    /**
     * Used to detect pause and stops in game
     */
    @Override
    public void onPause() {
        super.onPause();
        tankView.pauseGame();
    }
}