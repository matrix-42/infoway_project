package com.example.ningli.signindemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mydb.db";
    public static final String TABLE_NAME = "LoginInfo";


    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private  DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_creat =  "create table if not exists " + TABLE_NAME +
                            " (Id integer primary key AUTOINCREMENT, UserName text, Password text, Type text)";
        sqLiteDatabase.execSQL(sql_creat);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
