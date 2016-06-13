package com.example.gacmy.suixinji.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gacmy.suixinji.R;
import com.example.gacmy.suixinji.bean.TagBean;
import com.example.gacmy.suixinji.dao.TagDao;
import com.example.gacmy.suixinji.utils.ActivityUtils;
import com.example.gacmy.suixinji.viewholder.TagViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {
    private List<TagBean> list_tag;
    private Context mContext;
    public TagAdapter(List<TagBean> beanList,Context context){
        list_tag = beanList;
        mContext = context;
    }
    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list_item, parent,false);
        TagViewHolder vh = new TagViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, final int position) {
        holder.tv_tag.setText(list_tag.get(position).getContent());
        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res =  new TagDao(mContext).deleteTag(list_tag.get(position));
                if(res){
                    list_tag.remove(position);
                    notifyDataSetChanged();
                }else{
                    ActivityUtils.toast((Activity)mContext,R.string.deltagfailed);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_tag.size();
    }
}
