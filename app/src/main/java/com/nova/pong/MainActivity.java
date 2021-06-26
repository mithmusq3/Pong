package com.nova.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Start_game = findViewById(R.id.startBtn);
        Start_game.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startGame();
                    }

                    }


        );
    }

   private void startGame(){

       Intent i = new Intent(MainActivity.this,GameActivity.class);
       startActivity(i);

   }



}