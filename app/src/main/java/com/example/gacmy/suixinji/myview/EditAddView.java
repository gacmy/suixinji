package com.example.gacmy.suixinji.myview;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.gacmy.suixinji.R;

import com.example.gacmy.suixinji.utils.AppCompact;

/**
 * Created by Administrator on 2016/6/13.
 */
public class EditAddView extends FrameLayout{
    private EditText et;
    private ImageView iv;
    private GetTextListener mListener;

    public interface GetTextListener {
        public void getText(String text);
    }
    public void setGetTextListener(GetTextListener listener){
        mListener = listener;
    }
    public EditAddView(Context context) {
        super(context);
        init(context);
    }

    public EditAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.add_edit,null);
        iv = (ImageView)view.findViewById(R.id.iv_add_tag);
        AppCompact.setTint((Activity)context,iv,R.drawable.ic_add,R.color.green,R.color.colorAccent);
        et = (EditText)view.findViewById(R.id.et_tag);
        addView(view,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    iv.setVisibility(VISIBLE);
                } else {
                    iv.setVisibility(GONE);
                }
            }
        });

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.getText(et.getText().toString());
            }
        });
    }
}
