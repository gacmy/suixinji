package com.example.gacmy.suixinji.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gacmy.suixinji.bean.TagBean;
import com.example.gacmy.suixinji.dao.NoteDao;
import com.example.gacmy.suixinji.dao.TagDao;

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
        TagDao.createTable(db);
        initData(db);
    }

    //初始化标签数据
    private void initData(SQLiteDatabase db){
        TagBean tagBean1 = new TagBean();
        tagBean1.setContent("生活");
        TagBean tagBean2 = new TagBean();
        tagBean2.setContent("学习");
        TagBean tagBean3 = new TagBean();
        tagBean3.setContent("工作");
        TagBean tagBean4 = new TagBean();
        tagBean4.setContent("爱好");
        TagBean tagBean5 = new TagBean();
        tagBean5.setContent("娱乐");
        TagDao.insertBean(db, tagBean1);
        TagDao.insertBean(db,tagBean2);
        TagDao.insertBean(db,tagBean3);
        TagDao.insertBean(db,tagBean4);
        TagDao.insertBean(db,tagBean5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
