package com.example.gacmy.suixinji.myview.calendarview;


import java.util.HashMap;
import java.util.Map;


/**
 * Decorate several days with a dot
 */
public class EventDecorator {
     private HashMap<String,String> hashMap;

    public EventDecorator(Map<String, String> map){
        if(map == null){
            return;
        }
        hashMap = (HashMap)map;
   }
    public void setMap(Map<String ,String> map){
        hashMap = (HashMap)map;


    }


    //返回当前的dayView 改变样式

    public String shouldDecorateGAC(CalendarDay day) {
        String currentMode = -1+"";
        String str = DateUtils.getDateStr(day.getDate());

        if(hashMap == null){
            //Log.e("gac","hashmap is null");
            return "-1";
        }
      //  Log.e("gac", "date:" + str);
        if(hashMap.containsKey(str)){
            //Log.e("gac", "currentMode:" + currentMode);
            currentMode = hashMap.get(str);

        }
        return currentMode;

    }





}
