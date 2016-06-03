package com.example.gacmy.suixinji.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gacmy.suixinji.dao.NoteDao;

/**
 * Created by gac on 2016/6/2.
 */
public class SQLHelper extends SQLiteOpenHelper{
    public static String DB_NAME = "note.db";
    public static int VERSION = 1;
    private static SQLHelper instance;

    public static SQLHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLHelper(context.getApplicationContext());
        }
        return instance;
    }

    private SQLHelper(Context context){
        super(context,DB_NAME,null,VERSION);
        //this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        NoteDao.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
