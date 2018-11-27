package com.naveed.myplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_plan.db";
    public static final String TABLE_AIM = "aim_table";
    public static final String TABLE_DIET = "diet_table";
    public static final String TABLE_MEETINGS = "meetings_table";
    public static final String TABLE_EXERCISE = "exercise_table";
    public static final String TABLE_MEDICINE = "medicine_table";
    public static final String TABLE_EXPANCES = "expances_table";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";

    SQLiteDatabase db;


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME , null , 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("create table "+TABLE_AIM+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");

        db.execSQL("create table "+TABLE_DIET+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");

        db.execSQL("create table "+TABLE_MEETINGS+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");

        db.execSQL("create table "+TABLE_EXERCISE+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");

        db.execSQL("create table "+TABLE_MEDICINE+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");

        db.execSQL("create table "+TABLE_EXPANCES+" ( "+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TITLE+"  TEXT , "+COL_DESC+" TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_AIM);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DIET);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MEETINGS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EXPANCES);
        onCreate(db);
    }

    public boolean insertData(String title , String desc , String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE , title);
        contentValues.put(COL_DESC , desc);
        long result = db.insert(tableName , null , contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+tableName, null);

        return res;

    }




    public boolean updateTable(String id , String title , String desc , String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID , id);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESC , desc);
        long result = db.update(tableName , contentValues , "id = ?",new String[] {id});
        if (result == -1)
            return false;
        else
            return true;
    }



    public boolean doesTableExist(String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }


    public int DeleteData(String id , String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName , "id = ?",new String[]{id});
    }
}
