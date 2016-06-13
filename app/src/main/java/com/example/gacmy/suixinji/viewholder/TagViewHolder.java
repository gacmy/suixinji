package com.example.gacmy.suixinji.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gacmy.suixinji.R;

/**
 * Created by Administrator on 2016/6/13.
 */
public class TagViewHolder extends RecyclerView.ViewHolder {
    public ImageView iv_close;
    public TextView tv_tag;
    public TagViewHolder(View itemView) {
        super(itemView);
        iv_close = (ImageView)itemView.findViewById(R.id.iv_close);
        tv_tag = (TextView)itemView.findViewById(R.id.tv_tagcontent);
    }
}
