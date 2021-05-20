package com.example.testsingleactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDb";
    public static final String TABLE_USERS = "users";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SMILE = "smile";
    public static final String KEY_EXP = "exp";
    public static final String KEY_COUNT = "messageCount";

    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_USERS+ "("+ KEY_ID + " integer primary key autoincrement,"+KEY_NAME + " text ,"
                + KEY_SMILE+ " int,"+KEY_EXP+" text,"+KEY_COUNT+" text "+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_USERS);

        onCreate(db);
    }
}
