package com.example.gacmy.suixinji.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.adapter.MainTabViewPagerAdapter;
import com.example.gacmy.suixinji.bean.MessageEvent;
import com.example.gacmy.suixinji.bean.NoteEvent;
import com.example.gacmy.suixinji.fragment.SXBJFragment;
import com.example.gacmy.suixinji.fragment.WRBJFragment;
import com.example.gacmy.suixinji.myview.indicator.PagerSlidingTabStrip;
import com.example.gacmy.suixinji.myview.jumptext.JumpingBeans;
import com.example.gacmy.suixinji.utils.bitmap.BitmapUtils;
import com.example.gacmy.suixinji.utils.bitmap.ImageCompress;
import com.example.gacmy.suixinji.view.MainTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gacmy on 2016/5/30.
 */
public class MainTabActivity extends BaseActivity implements MainTabView{
    private ViewPager vp;
    private PagerSlidingTabStrip tabs;
    private Toolbar toolbar;

    private JumpingBeans jumpingBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithToolbar(R.layout.activity_maintab, toolbar);
        EventBus.getDefault().register(this);
    }
    public void initView(){
        vp = mGetView(R.id.viewpager);
        tabs = mGetView(R.id.tabs);
        toolbar = mGetView(R.id.maintoolbar);
        initPagerTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startJumpingText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopJumpingText();
    }



    /*接收WRBJFragment笔记信息数据 切换到笔记界面*/
    @Subscribe
    public void onEventMainThread(NoteEvent event) {
         if(event != null){
             vp.setCurrentItem(0,true);
         }
//        Log.e("gac", msg);
//        if(!TextUtils.isEmpty(msg)){
//            richTextEditor.insertImage(msg);
//        }
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    public void onClick(View v) {

    }


    @Override
    public void startJumpingText() {
         TextView tv_title = mGetView(R.id.tv_maintitle);
         jumpingBeans = JumpingBeans.with(tv_title)
                .makeTextJump(0, tv_title.getText().toString().length())
                .setIsWave(true)
                .setLoopDuration(1000)
                .build();
    }

    @Override
    public void stopJumpingText() {
        if(jumpingBeans != null){
            jumpingBeans.stopJumping();
        }
    }

    @Override
    public void sendGalleryPath(Uri uri) {
        if(uri != null){
            MessageEvent msg = new MessageEvent();
            msg.setMsg(ImageCompress.getImageAbsolutePath(this,uri));
            EventBus.getDefault().post(msg);
        }
    }

    public void sendCarmeraImgPath(Uri uri){
        if(uri != null){
            MessageEvent msg = new MessageEvent();
            msg.setMsg(ImageCompress.getImageAbsolutePath(this,uri));
            EventBus.getDefault().post(msg);
        }
    }
    //
    //接收相册 相机传入的值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("gac", "onActivityResult");
        Uri uri = BitmapUtils.getGalleryUri(data, requestCode);
        sendGalleryPath(uri);
        sendCarmeraImgPath(BitmapUtils.getCameraUri(requestCode));
    }

}
