package com.example.barca.pengogame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        Button start = (Button) findViewById(R.id.startButton);
        TextView text = findViewById(R.id.stateText);
        if(getIntent().getBooleanExtra("Win", false)){
            start.setText("NEXT LEVEL");
            text.setText("YOU WIN :)");
        }
        else {
            start.setText("TRY AGAIN");
            text.setText("YOU LOSE :(");
        }
    }
    public void onStartClicked(View view){
        Log.d("Start", "clicked");
        /*Intent Game = new Intent(GameEnd.this, Game.class);
        //finish();
         startActivity(Game);*/
        finish();
            }
    public void onMenuClicked(View view){
        Log.d("Menu", "clicked");

        Intent intent = new Intent(GameEnd.this, MainActivity.class);
        startActivity(intent);

    }
}
