package com.example.gacmy.suixinji.myview.dialogplus;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.example.gacmy.suixinji.R;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DialogUtils {

    public static void showOnlyContentDialog(Activity context,Holder holder,
                                             final OnClickListener okListener) {
        View titleLayout = LayoutInflater.from(context).inflate(R.layout.dialog_header,null);
        TextView titleView = (TextView)titleLayout.findViewById(R.id.dialog_title);
        titleView.setText("请选择标签");

        View footLayout = LayoutInflater.from(context).inflate(R.layout.dialog_footer,null);
        TextView canceView = (TextView)footLayout.findViewById(R.id.dialog_cancel);
        TextView okView = (TextView)footLayout.findViewById(R.id.dialog_ok);
        SimpleAdapter adapter = new SimpleAdapter(context, false);
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnClickListener(okListener)
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                    }
                })
                .setHeader(titleLayout)
                .setFooter(footLayout)
                .setExpanded(false)
                .setCancelable(false)

                .create();
        dialog.show();



    }
    public static void showNoHeaderDialog(Activity context,Holder holder, int gravity, BaseAdapter adapter,
                                    OnClickListener clickListener, OnItemClickListener itemClickListener,
                                    OnDismissListener dismissListener, OnCancelListener cancelListener,
                                    boolean expanded,int footerId) {
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setFooter(footerId)
                .setCancelable(true)
                .setGravity(gravity)
                .setAdapter(adapter)
                .setOnClickListener(clickListener)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("DialogPlus", "onItemClick() called with: " + "item = [" +
                                item + "], position = [" + position + "]");
                    }
                })
                .setOnDismissListener(dismissListener)
                .setOnCancelListener(cancelListener)
                .setExpanded(expanded)
                .create();
        dialog.show();
    }
    public static void showDialog(){

    }
}
