package com.example.gacmy.suixinji.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.adapter.MainTabViewPagerAdapter;
import com.example.gacmy.suixinji.fragment.SXBJFragment;
import com.example.gacmy.suixinji.fragment.WRBJFragment;
import com.example.gacmy.suixinji.myview.indicator.PagerSlidingTabStrip;
import com.example.gacmy.suixinji.view.MainTabView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gacmy on 2016/5/30.
 */
public class MainTabActivity extends AppCompatActivity implements MainTabView{
    private ViewPager vp;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintab);
        initView();
    }
    private void initView(){
        vp = (ViewPager)findViewById(R.id.viewpager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        initPagerTab();
    }
    //初始化viewpager
    public void initPagerTab() {
        List<Fragment> list = new ArrayList<>();
        list.add(new SXBJFragment());
        list.add(new WRBJFragment());
        vp.setAdapter(new MainTabViewPagerAdapter(getSupportFragmentManager(),list,this));
        tabs.setShouldExpand(true);
        tabs.setViewPager(vp);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        vp.setPageMargin(pageMargin);
        vp.setCurrentItem(1);
    }
}
