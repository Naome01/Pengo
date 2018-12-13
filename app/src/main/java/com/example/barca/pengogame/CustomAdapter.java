package com.example.barca.pengogame;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Vector;

import java.util.zip.Inflater;

public class CustomAdapter extends BaseAdapter {
    Context context;
    private Vector<String> Logins;
    private Vector<String> scores;
    private String actUser;
    LayoutInflater inflter;
    public static final String PENGO_COLUMN_NAME = "name";
    public static final String PENGO_COLUMN_SCORE = "score";
    private String user;

    public CustomAdapter(Context applicationContext) {
        this.context = applicationContext;
        SharedPreferences mySharePref = context.getSharedPreferences(context.getString(R.string.shared_pref) , context.MODE_PRIVATE);

        actUser = mySharePref.getString("Login", "");

        inflter = (LayoutInflater.from(applicationContext));
        DBHelper pengoDB = new DBHelper(context);
        Logins = new Vector<>();
        scores = new Vector<>();
        Cursor cursor = pengoDB.getScores();
        Logins.add("Login");
        scores.add("Score");
        cursor.moveToFirst();
        do{
            try{
                Logins.add(cursor.getString(cursor.getColumnIndexOrThrow(PENGO_COLUMN_NAME)));
                scores.add(cursor.getString(cursor.getColumnIndexOrThrow(PENGO_COLUMN_SCORE)));

                //cursor.moveToNext();
            }
            catch (Exception e){
                //i--;
               // cursor.moveToPrevious();
                Log.d("TestScores", e.getMessage());

            }
        }while (cursor.moveToNext());


    }

    @Override
    public int getCount() {
        return Logins.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_list, null);


        TextView login = (TextView) view.findViewById(R.id.textView);
        TextView score = (TextView) view.findViewById(R.id.textView2);

        if(Logins.get(i).equals(actUser)){
            login.setTextColor(Color.RED);
            score.setTextColor(Color.RED);
            login.setText(Logins.get(i));
            score.setText(scores.get(i));

        }
        else {
            login.setText(Logins.get(i));
            score.setText(scores.get(i));
        }
        return view;
    }
}