package com.hussain.irshadi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                Intent in=new Intent(getApplicationContext(),dashboard.class);

                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);


            }
        };
        handler.postDelayed(r, 3000);



    }
}
