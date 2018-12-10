package com.example.barca.pengogame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by radosek on 8/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "PengoDB";
    public static final String PENGO_TABLE_NAME = "users";
    public static final String PENGO_COLUMN_NAME = "name";
    public static final String PENGO_COLUMN_PASS = "pass";

    public static ArrayList<String> arrayList = new ArrayList<String>();

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table contacts " + "(id integer primary key, name text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + " (" + PENGO_COLUMN_NAME + "TEXT PRIMARY KEY, " + PENGO_COLUMN_PASS + " TEXT)");
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
        db.insert(PENGO_TABLE_NAME, null, contentValues);
        return true;
    }

    //Cursor representuje vracena data
    public String getPass(String login){
        String pass;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from users where name = ?" + "", new String[] {login});
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

    public boolean updateContact (Integer id, String name)
    {
        //TODO update zaznam
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
        /*SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, "1", null);*/
    }


}
