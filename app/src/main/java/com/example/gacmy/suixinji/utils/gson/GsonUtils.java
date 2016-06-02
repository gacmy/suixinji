package com.example.gacmy.suixinji.utils.gson;

import com.example.gacmy.suixinji.bean.NoteBean;
import com.example.gacmy.suixinji.myview.richedittext.RichTextEditor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by gac on 2016/6/2.
 */
public class GsonUtils {
    public static String list2JsonStr(List<RichTextEditor.EditData> beanList){
        Gson gson = new Gson();
        return gson.toJson(beanList);
    }
    public static List<RichTextEditor.EditData> jsonStr2List(String content){
        Gson gson = new Gson();
        return gson.fromJson(content, new TypeToken<List<RichTextEditor.EditData>>(){}.getType());
    }
}
