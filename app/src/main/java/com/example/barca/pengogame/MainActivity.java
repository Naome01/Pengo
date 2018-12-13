package com.example.barca.pengogame;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {


    public static final String PENGO_COLUMN_SCORE = "score";
    public static final String PENGO_COLUMN_LEVEL = "level";
    public static final String PENGO_COLUMN_LIVES = "lives";

    private String user;

    private SharedPreferences mySharePref;
    private SharedPreferences.Editor myShareEdit;
    private DBHelper pengoDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharePref = getSharedPreferences(getApplicationContext().getString(R.string.shared_pref) , getApplicationContext().MODE_PRIVATE);

        String prefUser = mySharePref.getString("Login", "");

        try{
        user = getIntent().getStringExtra("Login");}
        catch (Exception e){
            //user = prefUser;
        }
        if(user == null) user = prefUser;
        TextView loginTex = findViewById(R.id.loginText);
        int score = getIntent().getIntExtra("Score", 0);
        loginTex.setText("Logged as: " + user);
        if (prefUser.equals("")) prefUser = user;
        if(user != prefUser){
            pengoDb = new DBHelper(this.getApplicationContext());
            int lives = 4;
            int level = 0;
            Cursor res = pengoDb.getUserInfo(user);
            try {
                res.moveToFirst();
                lives = res.getInt(res.getColumnIndexOrThrow(PENGO_COLUMN_LIVES));
                level = res.getInt(res.getColumnIndexOrThrow(PENGO_COLUMN_LEVEL));

                if (res != null)
                    res.close();

            }
            catch (Exception e){
                Log.d("GetInfoTest", e.getMessage());

            }

            myShareEdit = mySharePref.edit();
            myShareEdit.putString("Login", user);
            myShareEdit.putString("Pass", pengoDb.getPass(user));
            myShareEdit.putInt("Level", level);
            myShareEdit.putInt("Lives", lives);


            myShareEdit.apply();

            }
    }
    public void OnStartClick(View view){
        Intent Game = new Intent(MainActivity.this, Game.class);
        MainActivity.this.startActivity(Game);
    }
    public void OnScoreClick(View view){
        Intent ScoresView = new Intent(MainActivity.this, ScoresView.class);
        ScoresView.putExtra("Login", user);
        MainActivity.this.startActivity(ScoresView);
    }

}
