package com.example.gacmy.suixinji.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gacmy.suixinji.bean.NoteBean;
import com.example.gacmy.suixinji.db.SQLHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 2016/6/2.
 */
public class NoteDao {
    public static String TABLE_NAME = "note";
    public static String COLUMN_DATETIME = "datetime";
    public static String COLUMN_CONTENT = "content";
    public static String COLUMN_ID = "id";
    public static String COLUMN_TAG = "tag";
    public static String NOTE_TABLE_CREATE = "create table if not exists "
            +TABLE_NAME +" ("
            +COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_DATETIME+" TEXT,"
            +COLUMN_CONTENT+" TEXT,"
            +COLUMN_TAG +" TEXT"+")";

      static String[] str_column = {COLUMN_DATETIME,COLUMN_CONTENT,COLUMN_TAG};
      static String[] int_cloumn = {COLUMN_ID};

    public static void createTable(SQLiteDatabase db){
        db.execSQL(NOTE_TABLE_CREATE);
    }
    public static void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE " +  "IF EXISTS "  + " note";
        db.execSQL(sql);
    }

    private SQLHelper helper;

    public NoteDao(Context context){
        helper = SQLHelper.getInstance(context);
    }

    //删除bean
    public boolean deleteBean(String wereClause,String[] whereArgs){
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            int res = db.delete(TABLE_NAME,wereClause,whereArgs);
            flag = (res != 0?true:false);
            if(db != null){
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
    //插入数据
    public boolean insertBean(NoteBean bean){
        boolean flag = false;
        SQLiteDatabase database = null;
        long id = -1;
        try{
            database = helper.getWritableDatabase();
            ContentValues values = getContentValues(bean);
            String tableName = TABLE_NAME;
            id = database.insert(tableName,null,values);
            flag = (id !=-1?true:false);
            if(database != null){
                database.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("gac","insertbean flag:"+flag);
        return flag;
    }


    //更新数据库
    public boolean updateBean(String whereClause,String[] whereArgs,NoteBean bean){
//        String wereClause = "id = ? and name = ?";
//        String [] whereArgs = new String[]{id,name};
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            ContentValues values = getContentValues(bean);
            int res = db.update(TABLE_NAME,values,whereClause,whereArgs);
            flag = (res <= 0?false:true);
            db.close();
        }catch (Exception e){e.printStackTrace();}
        Log.e("gac","updatebean:"+flag);
        return flag;
    }

    //查询
    public List<NoteBean>  getBeanList(String sql,String[] whereArgs){
        Log.e("gac","!!!!!!!!!!!!!!!");
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.e("gac","getBeanList");
        Cursor cursor = db.rawQuery(sql, whereArgs);
        Log.e("gac","************");
        List<NoteBean> mList = null;
        if(cursor != null){
            Log.e("gac","cursor is not null");
            mList = new ArrayList<>();
            while (cursor.moveToNext()){
                mList.add(getUserBeanByCursor(cursor));
            }
        }
        return mList;
    }

    //将bean 转换为contentValues
    private ContentValues getContentValues(NoteBean bean){
        ContentValues values = new ContentValues();
        Class<? extends  NoteBean> clazz = bean.getClass();
        try{
            Method[] methods = clazz.getMethods();
            Object[] args = null;
            for(Method method : methods) {
                String mName = method.getName();
                if (mName.startsWith("get") && !mName.startsWith("getClass")) {
                    String filedName = mName.substring(3, mName.length()).toLowerCase();
                    Log.e("gac", "fieldName:" + filedName);
                    if(filedName.equals("id")){
                        //id 让它自动增加
                        continue;
                    }
                    Object value = method.invoke(bean, args);
                    if (value instanceof String) {
                        values.put(filedName, (String) value);
                    } else if (value instanceof Integer) {
                        values.put(filedName, (Integer) value);
                    }
                }
            }
        }catch (Exception e){e.printStackTrace();}
        return values;
    }


    //根据cursor获取bean cursor 转换为bean
    private NoteBean getUserBeanByCursor(Cursor cursor){
        NoteBean bean = new NoteBean();
        Class< ? extends NoteBean> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields){
            for(int i = 0; i < str_column.length; i++){
                if(field.getName().equals(str_column[i])){
                    try {
                        field.set(str_column[i], cursor.getString(cursor.getColumnIndex(str_column[i])));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            for(int i = 0; i < int_cloumn.length; i++){
                if(field.getName().equals(int_cloumn[i])){
                    try {
                        field.set(int_cloumn[i], cursor.getInt(cursor.getColumnIndex(int_cloumn[i])));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return bean;
    }
}
