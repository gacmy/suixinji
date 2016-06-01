package com.example.gacmy.suixinji.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.utils.ActivityUtils;

import java.util.List;

/**
 * Created by gacmy on 2016/5/30.
 */
public class MainTabViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;
    public  String[] titles ;//"随心笔记","往日笔记"};

     public MainTabViewPagerAdapter(FragmentManager fm,List<Fragment> list,Context context){
         super(fm);
         this.list_fragment = list;
         titles = ActivityUtils.getArray(context,R.array.mainTabTitles);
     }
    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
