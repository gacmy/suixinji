package com.example.gacmy.suixinji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Administrator on 2016/1/10.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {



    private boolean mIsInitData;//是否初始化数据



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Bmob.initialize(this,"962d55e06ba8e9c7f2e75d32298b8357");
    }
//   public  void setContentViewNormal(int layoutResId){
//       setContentView(layoutResId);
//       initView();
//   }
//    public void setContentViewNoTitle(int layoutResId){
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(layoutResId);
//        initView();
//
//    }
    public void setContentViewWithToolbar(int layoutResId,Toolbar toolbar){
        setContentView(layoutResId);
        initView();
        setSupportActionBar(toolbar);
    }
//    public void setContentViewWithTitleBar(int layoutResId,int titlebarId){
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        setContentView(layoutResId);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, titlebarId);  //titlebar为自己标题栏的布局
//        initView();
//
//    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {
            if (!mIsInitData) {
                initData();
                mIsInitData = true;
            }
        }

        super.onWindowFocusChanged(hasFocus);
    }

    protected void initData() {

    }

    protected abstract void initView();

    /**

     * 获取View
     *
     * @param id
     * @return
     */
//通过反射直接获取各种view的对象
    @SuppressWarnings("unchecked")
    protected <T extends View> T mGetView(int id) {
        return (T) findViewById(id);
    }

    /**
     * 获取Button的实例 并绑定点击事件
     *
     * @param id
     * @return
     */
    protected Button mGetButtonSetOnClick(int id) {

        Button btn = (Button) findViewById(id);
        btn.setOnClickListener(this);
        return btn;
    }

    public final  boolean isEmpty(EditText et){
        if(TextUtils.isEmpty(et.getText().toString().trim())){
            return  true;
        }
        return false;
    }

    /**
     * 获取ImageView的实例 并绑定点击事件
     *
     * @param id
     * @return
     */
    protected ImageView mGetImageViewSetOnClick(int id) {

        ImageView image = (ImageView) findViewById(id);
        image.setOnClickListener(this);
        return image;
    }

    /**
     * 获取View的实例 并绑定点击事件
     *通过反射直接设置点击监听事件 并且获取对象 前面ImageView 和button 就有点多余了
     * @param id
     * @return
     */
    protected <T extends View> T mGetViewSetOnClick(int id) {

        T view = (T) findViewById(id);
        view.setOnClickListener(this);
        return (T)view;
    }

    /**
     * 获取TextView中的文本信息
     *
     * @param tv
     * @return
     */
    protected String mGetTextViewContent(TextView tv) {
        return tv.getText().toString().trim();
    }

    /**
     * 获取EditText中的文本信息
     *
     * @param et
     * @return
     */
    protected String mGetEditTextContent(EditText et) {
        return et.getText().toString().trim();
    }
    // 对toast分装 可以少写很多代码
    protected void print(int sid) {

        Toast.makeText(this, getResources().getString(sid), Toast.LENGTH_SHORT).show();
    }

    protected void print(String sMsg) {

        Toast.makeText(this,sMsg,Toast.LENGTH_SHORT).show();
    }

    public void e(String str){
        Log.e("gac",str);
    }

}
