package com.example.barca.pengogame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class GameHolder extends Activity {
    public String User;
    public int level;
    public int lives;

    private SharedPreferences mySharePref;
    private SharedPreferences.Editor myShareEdit;
    private Context cont;
    DBHelper db;


    GameHolder(Context context){
        cont = context;
        db = new DBHelper(cont);
        mySharePref = context.getSharedPreferences(cont.getString(R.string.shared_pref) , cont.MODE_PRIVATE);

        User = mySharePref.getString("Login", "");
        level = mySharePref.getInt("Level", 1);
        if(level == 0) level =1;
        lives = mySharePref.getInt("Lives", 4);

       // Log.d("GameHolder", User + level + lives);

    }
    public void SetPref(int lev, int liv){
        int tmpLives = liv;
        if(level < lev) {
            db.updateUser(User, 10, lev, liv);

        }
        else if (tmpLives<=0){
            db.updateUser(User, -15, lev, 4);
            tmpLives = 4;
        }
        myShareEdit = mySharePref.edit();
        myShareEdit.putInt("Level", lev);
        myShareEdit.putInt("Lives", tmpLives);
        myShareEdit.apply();
        level = lev;
        lives = tmpLives;
        Log.d("GameHolder", User + level + lives);

    }
}
