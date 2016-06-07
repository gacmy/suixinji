package com.example.gacmy.suixinji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.myview.CalendarSwitchView;

/**
 * Created by gacmy on 2016/5/30.
 * 往日笔记Fragment
 */
public class WRBJFragment extends BaseFragment{
    private CalendarSwitchView calendarSwitchView;
    private Button btn;

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_wrbj,container,false);
    }

    @Override
    public void initView(View view) {
        calendarSwitchView = mGetView(R.id.calendarswitchview,view);
        btn = mGetViewSetOnClick(R.id.btn,view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                calendarSwitchView.setTextYear1();
                break;
        }
    }
}
