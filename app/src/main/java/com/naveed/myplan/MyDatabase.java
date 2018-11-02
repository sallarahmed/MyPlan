package com.naveed.myplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "login.db";
    public static final String TABLE_NAME = "login_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_UNAME = "username";
    public static final String COL_PASSWORD = "password";


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME , null , 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_NAME+"  TEXT , "+COL_UNAME+" TEXT , "+COL_PASSWORD+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name , String uname , String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME , name);
        contentValues.put(COL_UNAME , uname);
        contentValues.put(COL_PASSWORD , pass);
        long result = db.insert(TABLE_NAME , null , contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME , null);

        return res;

    }

    public Cursor verifyCredentials(String uname , String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        // Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME, new String[]{name});
        Cursor cur =  db.query(TABLE_NAME,null,COL_UNAME+" = ? AND "+COL_PASSWORD+" = ?" ,
                new String[]{uname , pass},null,null,null);
        return cur;

    }

    public boolean updateData(String id , String name , String username , String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID , id);
        contentValues.put(COL_NAME , name);
        contentValues.put(COL_UNAME , username);
        contentValues.put(COL_PASSWORD , password);
        long result = db.update(TABLE_NAME , contentValues , "id = ?",new String[] {id});
        if (result == -1)
            return false;
        else
            return true;
    }

    public int DeleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME , "id = ?",new String[]{id});
    }
}
