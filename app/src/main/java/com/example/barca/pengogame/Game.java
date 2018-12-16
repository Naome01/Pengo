package com.example.barca.pengogame;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(this).registerReceiver(new OverHandler(),
                new IntentFilter("Menu"));

        super.onCreate(savedInstanceState);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game);

    }
    private void killActivity() {
        Intent intent = new Intent(Game.this, MainActivity.class);
        startActivity(intent);
    }

    public class OverHandler extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            //finish();
        }}

}