package com.example.gacmy.suixinji.fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.myview.CalendarSwitchView;
import com.example.gacmy.suixinji.myview.calendarview.CalendarDay;
import com.example.gacmy.suixinji.myview.calendarview.MaterialCalendarView;

/**
 * Created by gacmy on 2016/5/30.
 * 往日笔记Fragment
 */
public class WRBJFragment extends BaseFragment implements
        MaterialCalendarView.OnDateSelectedListener,MaterialCalendarView.OnMonthChangedListener{
    private CalendarSwitchView calendarSwitchView;
    private Button btn;
    private MaterialCalendarView calendarView;
    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_wrbj,container,false);
    }

    @Override
    public void initView(View view) {
        calendarSwitchView = mGetView(R.id.calendarswitchview,view);
        btn = mGetViewSetOnClick(R.id.btn,view);
        calendarView = mGetView(R.id.calendarview,view);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:

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
