package com.example.barca.pengogame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ViewDebug;

import java.util.ArrayList;

/**
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "PengoDB";
    public static final String PENGO_TABLE_NAME = "users";
    public static final String PENGO_COLUMN_NAME = "name";
    public static final String PENGO_COLUMN_PASS = "pass";
    public static final String PENGO_COLUMN_SCORE = "score";
    public static final String PENGO_COLUMN_LEVEL = "level";
    public static final String PENGO_COLUMN_LIVES = "lives";




    public static ArrayList<String> arrayList = new ArrayList<String>();

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table contacts " + "(id integer primary key, name text)");
        db.execSQL("CREATE TABLE " + PENGO_TABLE_NAME + " (" + PENGO_COLUMN_NAME + " TEXT PRIMARY KEY, " + PENGO_COLUMN_PASS + " TEXT, " + PENGO_COLUMN_SCORE + " INTEGER, " + PENGO_COLUMN_LEVEL + " INTEGER, "+ PENGO_COLUMN_LIVES + " INTEGER)");
        //insertUser("Admin", "admin");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean insertUser(String name, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PENGO_COLUMN_NAME, name);
        contentValues.put(PENGO_COLUMN_PASS, pass);
        contentValues.put(PENGO_COLUMN_SCORE, 0);

        db.insert(PENGO_TABLE_NAME, null, contentValues);
        return true;
    }

    //Cursor representuje vracena data
    public String getPass(String login){
        String pass;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select pass from users where name = ?" + "", new String[] {login});
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );
        try {
            res.moveToFirst();
            pass = res.getString(res.getColumnIndexOrThrow(PENGO_COLUMN_PASS));
            if (res != null)
                res.close();
            Log.d("LoginTest", pass);

            return pass;
        }
        catch (Exception e){
            Log.d("LoginTest", e.getMessage());
            Log.d("LoginTest", String.valueOf(res.getColumnIndexOrThrow(PENGO_COLUMN_PASS)));

            return "0";

        }
    }
    public Integer getHighestScore(String login){
        Integer score;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select score from users where name = ?" + "", new String[] {login});
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );
        try {
            res.moveToFirst();
            score = res.getInt(res.getColumnIndexOrThrow(PENGO_COLUMN_SCORE));
            if (res != null)
                res.close();
            Log.d("LoginTest", score.toString());

            return score;
        }
        catch (Exception e){
            Log.d("LoginTest", e.getMessage());
            Log.d("LoginTest", String.valueOf(res.getColumnIndexOrThrow(PENGO_COLUMN_SCORE)));

            return 0;

        }
    }
    public Cursor getUserInfo(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery("select * from users where name = ?" + "", new String[] {name});
        return res;
    }
    public Cursor getScores(){

        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor res =  db.rawQuery("select * from users order by " + PENGO_COLUMN_SCORE, null);
        Cursor res =  db.rawQuery("select * from users order by score desc" + "", null);
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );
        return res;
    }
    public Cursor getLastState(String name){

        SQLiteDatabase db = this.getReadableDatabase();
        // Cursor res =  db.rawQuery("select * from users order by " + PENGO_COLUMN_SCORE, null);
        Cursor res =  db.rawQuery("select * from users where name = ?" + "", new String[]{name});
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );
        return res;
    }

    public boolean updateUser (String name, int score, int level, int lives)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + PENGO_TABLE_NAME + " SET " + PENGO_COLUMN_LEVEL + " = ? , " + PENGO_COLUMN_LIVES+ " = ? , " + PENGO_COLUMN_SCORE + " = score + ? WHERE " + PENGO_COLUMN_NAME +" = ?" + "", new String[]{String.valueOf(level), String.valueOf(lives), String.valueOf(score), name});
        return true;
    }

    public void setAllContacs()
    {
        /*arrayList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }*/
    }

    public ArrayList<String> getAllContacsName()
    {
        return arrayList;
    }

    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PENGO_TABLE_NAME, "1", null);
    }


}
