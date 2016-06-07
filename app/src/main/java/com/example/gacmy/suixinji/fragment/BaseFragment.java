package com.example.gacmy.suixinji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gacmy.suixinji.myview.toast.GacToast;

/**
 * Created by Administrator on 2016/6/1.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private int resId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getView(inflater,container);
        initView(view);
        initEvent();
        return view;
    }
    public abstract View getView(LayoutInflater inflater,ViewGroup container);
    public abstract void initView(View view);

    public void initEvent(){

    }
    protected <T extends View> T mGetView(int id,View view) {
        return (T) view.findViewById(id);
    }

    protected <T extends View> T mGetViewSetOnClick(int id,View v) {
        T view = (T) v.findViewById(id);
        view.setOnClickListener(this);
        return (T)view;
    }

    protected void toast(String content){
        GacToast.makeText(getActivity(),content).show();
    }
}
