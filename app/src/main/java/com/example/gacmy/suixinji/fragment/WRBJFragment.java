package com.example.gacmy.suixinji.fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.activity.TagActivity;
import com.example.gacmy.suixinji.bean.NoteEvent;
import com.example.gacmy.suixinji.myview.CalendarSwitchView;
import com.example.gacmy.suixinji.myview.calendarview.CalendarDay;
import com.example.gacmy.suixinji.myview.calendarview.EventDecorator;
import com.example.gacmy.suixinji.myview.calendarview.MaterialCalendarView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gacmy on 2016/5/30.
 * 往日笔记Fragment
 */
public class WRBJFragment extends BaseFragment implements
        MaterialCalendarView.OnDateSelectedListener,MaterialCalendarView.OnMonthChangedListener{
    private CalendarSwitchView calendarSwitchView;
    private Button btn;
    private MaterialCalendarView calendarView;
    private EventDecorator decorator;
    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_wrbj,container,false);
    }

    @Override
    public void initView(View view) {
        calendarSwitchView = mGetView(R.id.calendarswitchview,view);
        btn = mGetViewSetOnClick(R.id.btn, view);
        decorator = new EventDecorator(null);
        calendarView = mGetView(R.id.calendarview,view);
        calendarView.setOnDateChangedListener(this);
        calendarView.addDecoratorGAC(decorator);
        calendarView.setOnMonthChangedListener(this);
        decorator.setMap(getMap());
    }

    public Map<String,String> getMap(){
        HashMap<String,String> map = new HashMap<>();
        map.put("2016-06-10","1");
        map.put("2016-06-12","1");
        map.put("2016-06-14","1");
        map.put("2016-06-20","1");
        map.put("2016-06-29","1");
        return map;
    }
    //显示当前笔记信息 跳转到SXBJFragment界面
    private void showCurrentNote(){
        EventBus.getDefault().post(new NoteEvent());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                //showCurrentNote();
                startActivity(new Intent(getActivity(), TagActivity.class));
                break;
        }
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        calendarSwitchView.setDateChanged(date.getYear(),date.getMonth()+1,date.getDay());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        calendarSwitchView.setDateChanged(date.getYear(),date.getMonth()+1,date.getDay());
    }
}
