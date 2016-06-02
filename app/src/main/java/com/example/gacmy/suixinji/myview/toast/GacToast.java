package com.example.gacmy.suixinji.myview.toast;

import android.app.Activity;
import android.content.res.Resources;

import android.graphics.Color;

import android.graphics.Typeface;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by Administrator on 2016/4/7.
 */
public class GacToast {
    private Activity activity;
    private ViewGroup viewGroup;
    private FrameLayout croutonView;
    private Animation inAnimation;
    private Animation outAnimation;
    private static final int holoRedLight = 0xffff4444;
    private static final int holoGreenLight = 0xff99cc00;
    private static final int holoBlueLight = 0xff33b5e5;
    private  int color;
    public  static int ERROR = 0;
    public static int INFO = 1;

    private static final int TEXT_ID = 0x101;
    CharSequence text;

    public static GacToast makeText(Activity activity, CharSequence text, int style) {
        return new GacToast(activity, text, style);
    }
    public static GacToast makeText(Activity activity,CharSequence text){
        return new GacToast(activity,text,-1);
    }
    private GacToast(Activity activity,CharSequence text,int style){
        this.activity = activity;
        this.viewGroup = null;
        this.text = text;
        if(style == INFO){
            color = holoBlueLight;
        }else if(style == ERROR){
            color = holoRedLight;
        }else{
            color = holoGreenLight;
        }
    }
    public void show() {
        Log.e("gac", "show method");
        GacManager.getInstance().add(this);
    }
    public Activity getActivity(){
        return this.activity;
    }

    View getView() {


        // if already setup return the view
        if (null == this.croutonView) {
            initializeCroutonView();
        }

        return croutonView;
    }
    private void initializeCroutonView() {
        Resources resources = this.activity.getResources();

        this.croutonView = initializeCroutonViewGroup(resources);

        // create content view
        RelativeLayout contentView = initializeContentView(resources);
        this.croutonView.addView(contentView);
    }

    public CharSequence getText(){
        return this.text;
    }

    public Animation getInAnimation() {
        if ((null == this.inAnimation) && (null != this.activity)) {
                measureCroutonView();
                this.inAnimation = new DefaultAnimationsBuilder().buildDefaultSlideInDownAnimation(getView());
        }

        return inAnimation;
    }

    boolean isShowing() {
        return (null != activity) && (isCroutonViewNotNull());
    }

    private boolean isCroutonViewNotNull() {
        return (null != croutonView) && (null != croutonView.getParent());
    }

    private void measureCroutonView() {
        View view = getView();
        int widthSpec;
        if (null != viewGroup) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        } else {
            widthSpec = View.MeasureSpec.makeMeasureSpec(activity.getWindow().getDecorView().getMeasuredWidth(),
                    View.MeasureSpec.AT_MOST);
        }

        view.measure(widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private FrameLayout initializeCroutonViewGroup(Resources resources) {
        FrameLayout croutonView = new FrameLayout(this.activity);
        final int height;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
         int width = 0;
        croutonView.setLayoutParams(
                new FrameLayout.LayoutParams(width != 0 ? width : FrameLayout.LayoutParams.MATCH_PARENT, height));
        croutonView.setBackgroundColor(color);
         return croutonView;
    }

    private RelativeLayout initializeContentView(final Resources resources) {
        RelativeLayout contentView = new RelativeLayout(this.activity);
        contentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        int padding = 10;
        contentView.setPadding(padding, padding, padding, padding);
        TextView text = initializeTextView(resources);
         RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
         RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
         contentView.addView(text, textParams);
         return contentView;
    }

    private TextView initializeTextView(final Resources resources) {
        TextView text = new TextView(this.activity);
        text.setId(TEXT_ID);
        text.setText(this.text);
        text.setTypeface(Typeface.DEFAULT_BOLD);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(Color.WHITE);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        return text;
    }



    public Animation getOutAnimation() {
         this.outAnimation = new DefaultAnimationsBuilder().buildDefaultSlideOutUpAnimation(getView());
         return outAnimation;
    }
    void detachActivity() {
        activity = null;
    }

    void detachViewGroup() {
        viewGroup = null;
    }



     class DefaultAnimationsBuilder {
        private static final long DURATION = 400;
        private    Animation slideInDownAnimation, slideOutUpAnimation;
        private   int lastInAnimationHeight, lastOutAnimationHeight;

        public DefaultAnimationsBuilder() {
    /* no-op */
        }

        /**
         * @param croutonView
         *   The croutonView which gets animated.
         *
         * @return The default Animation for a showing {@link Crouton}.
         */
         Animation buildDefaultSlideInDownAnimation(View croutonView) {
            if (!areLastMeasuredInAnimationHeightAndCurrentEqual(croutonView) || (null == slideInDownAnimation)) {
                slideInDownAnimation = new TranslateAnimation(
                        0, 0,                               // X: from, to
                        -croutonView.getMeasuredHeight(), 0 // Y: from, to
                );
                slideInDownAnimation.setDuration(DURATION);
                setLastInAnimationHeight(croutonView.getMeasuredHeight());
            }
            return slideInDownAnimation;
        }

        /**
         * @param croutonView
         *   The croutonView which gets animated.
         *
         * @return The default Animation for a hiding {@link Crouton}.
         */
         Animation buildDefaultSlideOutUpAnimation(View croutonView) {
            if (!areLastMeasuredOutAnimationHeightAndCurrentEqual(croutonView) || (null == slideOutUpAnimation)) {
                slideOutUpAnimation = new TranslateAnimation(
                        0, 0,                               // X: from, to
                        0, -croutonView.getMeasuredHeight() // Y: from, to
                );
                slideOutUpAnimation.setDuration(DURATION);
                setLastOutAnimationHeight(croutonView.getMeasuredHeight());
            }
            return slideOutUpAnimation;
        }

        private  boolean areLastMeasuredInAnimationHeightAndCurrentEqual(View croutonView) {
            return areLastMeasuredAnimationHeightAndCurrentEqual(lastInAnimationHeight, croutonView);
        }

        private  boolean areLastMeasuredOutAnimationHeightAndCurrentEqual(View croutonView) {
            return areLastMeasuredAnimationHeightAndCurrentEqual(lastOutAnimationHeight, croutonView);
        }

        private  boolean areLastMeasuredAnimationHeightAndCurrentEqual(int lastHeight, View croutonView) {
            return lastHeight == croutonView.getMeasuredHeight();
        }

        private  void setLastInAnimationHeight(int lastInAnimationHeight) {
            this.lastInAnimationHeight = lastInAnimationHeight;
        }

        private  void setLastOutAnimationHeight(int lastOutAnimationHeight) {
            this.lastOutAnimationHeight = lastOutAnimationHeight;
        }
    }

}
