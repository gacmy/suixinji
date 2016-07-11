package com.example.gacmy.suixinji.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gacmy.suixinji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taofangxin on 16/4/16.
 */
public class FlowLayout extends ViewGroup {

    private List<CheckTextView> tv_list = new ArrayList<>();
    public static String TAG = FlowLayout.class.getName();
    private Context mContext;
    private int usefulWidth; // the space of a line we can use(line's width minus the sum of left and right padding
    private int lineSpacing = 0; // the spacing between lines in flowlayout
    List<View> childList = new ArrayList();
    List<Integer> lineNumList = new ArrayList();
    public OnClickListener imgCloseListener;

    public FlowLayout(Context context) {
        this(context, null);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FlowLayout);
        lineSpacing = mTypedArray.getDimensionPixelSize(
                R.styleable.FlowLayout_lineSpacing, 0);
        mTypedArray.recycle();
        init();
    }
    private void init(){
//        imgCloseListener = new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int index = indexOfChild(v);
//                removeViewAt(index);
//                list_editableTag.remove(v.getTag());
//            }
//        };
    }

    public int i = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"onMeasure:"+i++);
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineY = mPaddingTop;
        int lineHeight = 0;

        //Log.e("gac","childCount:"+getChildCount());
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);

            //Log.e("gac", "childWidth1:" + child.getMeasuredWidth() + " childHeight1:" + child.getMeasuredHeight());
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;
            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, lineY);
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin;
                spaceHeight = mlp.topMargin + mlp.bottomMargin;
                Log.e("gac", "marginLayout width:" + spaceWidth + " height:" + spaceHeight);
            } else {
                Log.e("gac", "widthMeasureSpec:" + widthMeasureSpec + " heightMeasureSpec:" + heightMeasureSpec);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            Log.e("gac", "childWidth:" + childWidth + " childHeight:" + childHeight);
            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineUsed + spaceWidth > widthSize) {
                //approach the limit of width and move to next line
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineHeight = 0;
            }
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
        }
        setMeasuredDimension(
                widthSize,
                heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + mPaddingBottom
        );
    }

//    private List<View> list_editableTag = new ArrayList<>();
//    public void initTagEditable(String text){
//        String[] tagText = text.split(":");
//        for(int i = 0; i < tagText.length; i++){
//            View view = LayoutInflater.from(mContext).inflate(R.layout.del_tag_view,null);
//            view.setTag(i);
//            ImageView iv = (ImageView)view.findViewById(R.id.iv_close);
//            iv.setTag(i);
//            TextView textView = (TextView)view.findViewById(R.id.tv_tag);
//            textView.setText(tagText[i]);
//            iv.setOnClickListener(imgCloseListener);
//            addView(view);
//            list_editableTag.add(view);
//        }
//
//    }
//
//    public void addEditableTag(String text){
//        View view = LayoutInflater.from(mContext).inflate(R.layout.del_tag_view,null);
//        view.setTag(list_editableTag.size());
//        ImageView iv = (ImageView)view.findViewById(R.id.iv_close);
//        iv.setTag(list_editableTag.size());
//        TextView textView = (TextView)view.findViewById(R.id.tv_tag);
//        textView.setText(text);
//        iv.setOnClickListener(imgCloseListener);
//        list_editableTag.add(view);
//    }

    public void setCheckedTags(String text){
        String[] tagText = text.split(":");
        //设置已经选择的标签
        for(int i = 0; i < tv_list.size();i++){
            for(int j = 0; j < tagText.length; j++){
                if (tv_list.get(i).getText().equals(tagText[j])){
                    tv_list.get(i).setCheckedBg();
                }
            }
        }
    }
    public void addTags(String text){
        String[] tagText = text.split(":");
        //添加新的标签
        for(int i = 0; i < tagText.length; i++){
            CheckTextView tv4 = new CheckTextView(mContext);
            tv4.setText(tagText[i]);
            tv4.setPadding(5, 5, 5, 5);
            tv4.setTextSize(16);
            tv4.setBackgroundResource(R.drawable.circle);
            addTag(tv4);
        }
    }
    public void addTag(CheckTextView tv){
        addView(tv);
        tv_list.add(tv);
    }


    public String getTagStr(){
        if(tv_list.size() <= 0){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tv_list.size(); i++){
            if(tv_list.get(i).isChecked){
                sb.append(tv_list.get(i).getText()+":");
            }
        }
        return sb.toString();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int lineX = mPaddingLeft;
        int lineY = mPaddingTop;
        int lineWidth = r - l;
        usefulWidth = lineWidth - mPaddingLeft - mPaddingRight;
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineHeight = 0;
        int lineNum = 0;

        lineNumList.clear();
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int spaceWidth = 0;
            int spaceHeight = 0;
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            LayoutParams childLp = child.getLayoutParams();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                spaceWidth = mlp.leftMargin + mlp.rightMargin+5;
                spaceHeight = mlp.topMargin + mlp.bottomMargin;
                left = lineX + mlp.leftMargin;
                top = lineY + mlp.topMargin;
                right = lineX + mlp.leftMargin + childWidth;
                bottom = lineY + mlp.topMargin + childHeight;
            } else {
                left = lineX;
                top = lineY;
                right = lineX + childWidth;
                bottom = lineY + childHeight;
            }
            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineUsed + spaceWidth > lineWidth) {
                //approach the limit of width and move to next line
                lineNumList.add(lineNum);
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineX = mPaddingLeft;
                lineHeight = 0;
                lineNum = 0;
                if (childLp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) childLp;
                    left = lineX + mlp.leftMargin;
                    top = lineY + mlp.topMargin;
                    right = lineX + mlp.leftMargin + childWidth;
                    bottom = lineY + mlp.topMargin + childHeight;
                } else {
                    left = lineX;
                    top = lineY;
                    right = lineX + childWidth;
                    bottom = lineY + childHeight;
                }
            }
            child.layout(left, top, right, bottom);
            lineNum ++;
            if (spaceHeight > lineHeight) {
                lineHeight = spaceHeight;
            }
            lineUsed += spaceWidth;
            lineX += spaceWidth;
        }
        // add the num of last line
        lineNumList.add(lineNum);
    }

    /**
     * resort child elements to use lines as few as possible
     */
    public void relayoutToCompress() {
        int childCount = this.getChildCount();
        if (0 == childCount) {
            //no need to sort if flowlayout has no child view
            return;
        }
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            count++;
        }
        View[] childs = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            childs[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp ;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            } else {
                spaces[n] = childWidth;
            }
            n++;
        }
        int[] compressSpaces = new int[count];
        for (int i = 0; i < count; i++) {
            compressSpaces[i] = spaces[i] > usefulWidth ? usefulWidth : spaces[i];
        }
        sortToCompress(childs, compressSpaces);
        this.removeAllViews();
        for (View v : childList) {
            this.addView(v);
        }
        childList.clear();
    }

    private void sortToCompress(View[] childs, int[] spaces) {
        int childCount = childs.length;
        int[][] table = new int[childCount + 1][usefulWidth + 1];
        for (int i = 0; i < childCount +1; i++) {
            for (int j = 0; j < usefulWidth; j++) {
                table[i][j] = 0;
            }
        }
        boolean[] flag = new boolean[childCount];
        for (int i = 0; i < childCount; i++) {
            flag[i] = false;
        }
        for (int i = 1; i <= childCount; i++) {
            for (int j = spaces[i-1]; j <= usefulWidth; j++) {
                table[i][j] = (table[i-1][j] > table[i-1][j-spaces[i-1]] + spaces[i-1]) ? table[i-1][j] : table[i-1][j-spaces[i-1]] + spaces[i-1];
            }
        }
        int v = usefulWidth;
        for (int i = childCount ; i > 0 && v >= spaces[i-1]; i--) {
            if (table[i][v] == table[i-1][v-spaces[i-1]] + spaces[i-1]) {
                flag[i-1] =  true;
                v = v - spaces[i - 1];
            }
        }
        int rest = childCount;
        View[] restArray;
        int[] restSpaces;
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == true) {
                childList.add(childs[i]);
                rest--;
            }
        }

        if (0 == rest) {
            return;
        }
        restArray = new View[rest];
        restSpaces = new int[rest];
        int index = 0;
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == false) {
                restArray[index] = childs[i];
                restSpaces[index] = spaces[i];
                index++;
            }
        }
        table = null;
        childs = null;
        flag = null;
        sortToCompress(restArray, restSpaces);
    }

    /**
     * add some blank view to make child elements look in alignment
     */
    public void relayoutToAlign() {
        int childCount = this.getChildCount();
        if (0 == childCount) {
            //no need to sort if flowlayout has no child view
            return;
        }
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            count++;
        }
        View[] childs = new View[count];
        int[] spaces = new int[count];
        int n = 0;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof BlankView) {
                //BlankView is just to make childs look in alignment, we should ignore them when we relayout
                continue;
            }
            childs[n] = v;
            LayoutParams childLp = v.getLayoutParams();
            int childWidth = v.getMeasuredWidth();
            if (childLp instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) childLp ;
                spaces[n] = mlp.leftMargin + childWidth + mlp.rightMargin;
            } else {
                spaces[n] = childWidth;
            }
            n++;
        }
        int lineTotal = 0;
        int start = 0;
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            if (lineTotal + spaces[i] > usefulWidth) {
                int blankWidth = usefulWidth - lineTotal;
                int end = i - 1;
                int blankCount = end - start;
                if (blankCount >= 0) {
                    if (blankCount > 0) {
                        int eachBlankWidth = blankWidth / blankCount;
                        MarginLayoutParams lp = new MarginLayoutParams(eachBlankWidth, 0);
                        for (int j = start; j < end; j++) {
                            this.addView(childs[j]);
                            BlankView blank = new BlankView(mContext);
                            this.addView(blank, lp);
                        }
                    }
                    this.addView(childs[end]);
                    start = i;
                    i --;
                    lineTotal = 0;
                } else {
                    this.addView(childs[i]);
                    start = i + 1;
                    lineTotal = 0;
                }
            } else {
                lineTotal += spaces[i];
            }
        }
        for (int i = start; i < count; i++) {
            this.addView(childs[i]);
        }
    }

    /**
     * use both of relayout methods together
     */
    public void relayoutToCompressAndAlign(){
        this.relayoutToCompress();
        this.relayoutToAlign();
    }

    /**
     * cut the flowlayout to the specified num of lines
     * @param line_num
     */
    public void specifyLines(int line_num) {
        int childNum = 0;
        if (line_num > lineNumList.size()) {
            line_num = lineNumList.size();
        }
        for (int i = 0; i < line_num; i++) {
            childNum += lineNumList.get(i);
        }
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < childNum; i++) {
            viewList.add(getChildAt(i));
        }
        removeAllViews();
        for (View v : viewList) {
            addView(v);
        }
    }
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    class BlankView extends View {

        public BlankView(Context context) {
            super(context);
        }
    }
}
