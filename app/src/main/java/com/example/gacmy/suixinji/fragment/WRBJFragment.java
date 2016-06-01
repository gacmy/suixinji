package com.example.gacmy.suixinji.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gacmy.suixinji.R;

/**
 * Created by gacmy on 2016/5/30.
 * 往日笔记Fragment
 */
public class WRBJFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wrbj,null);
        return view;
    }
}
