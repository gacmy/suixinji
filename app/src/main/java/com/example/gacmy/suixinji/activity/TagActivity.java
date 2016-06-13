package com.example.gacmy.suixinji.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.adapter.TagAdapter;
import com.example.gacmy.suixinji.bean.TagBean;
import com.example.gacmy.suixinji.dao.TagDao;
import com.example.gacmy.suixinji.myview.EditAddView;
import com.example.gacmy.suixinji.myview.itemdecoration.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gac on 2016/6/13.
 */
public class TagActivity extends BaseActivity {
    private Toolbar toolbar;
    private EditAddView addtagView;
    private RecyclerView rv_tag;
    private List<TagBean> list_tag;
    private TagAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithToolbar(R.layout.acitivity_addtag, toolbar);
    }

    @Override
    protected void initView() {
        toolbar = mGetView(R.id.backtoolbar);
        addtagView = mGetView(R.id.et_addtag);
        rv_tag = mGetView(R.id.rv_tag);
        addtagView.setGetTextListener(new EditAddView.GetTextListener() {
            @Override
            public void getText(String text) {
                TagBean bean = new TagBean();
                bean.setContent(text);
                long res  = new TagDao(getApplicationContext()).insertBean(bean);
                if(res != -1){
                    bean.setId((int)res);
                    list_tag.add(bean);
                    adapter.notifyDataSetChanged();
                }else{
                    toast(getString(R.string.addtagfailed));
                }
            }
        });
        initRecylerView();
    }
    private void initToolbar(){

    }

    private void initRecylerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tag.setLayoutManager(linearLayoutManager);
        rv_tag.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void initData() {
        super.initData();
        list_tag = new TagDao(this).getAllTagList();
        if(list_tag == null){
            list_tag = new ArrayList<>();
        }
        adapter = new TagAdapter(list_tag,this);
        rv_tag.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
}
