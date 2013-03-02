package com.vgershman.whocall.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    private List<String> fields;
    private String tableName;

    public DBHelper(Context context, String dbName, String tableName, List<String> fields) {
        super(context, dbName, null, 1);
        this.fields=fields;
        this.tableName=tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder=new StringBuilder();
        builder.append("create table ");
        builder.append(tableName);
        builder.append("(id integer primary key autoincrement,");
        for(String field:fields){
            builder.append(field);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append(");");

        db.execSQL(builder.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}