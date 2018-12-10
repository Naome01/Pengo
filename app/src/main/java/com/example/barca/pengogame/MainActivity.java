package com.example.barca.pengogame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("Login");
        TextView loginTex = findViewById(R.id.loginText);
        loginTex.setText("Logged as: " + user);
    }
    public void OnStartClick(View view){
        Intent Game = new Intent(MainActivity.this, Game.class);
        MainActivity.this.startActivity(Game);
    }
}
