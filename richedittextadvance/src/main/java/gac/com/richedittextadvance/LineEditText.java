package gac.com.richedittextadvance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/6/3.
 */
public class LineEditText extends EditText {
    private Paint paint;
    public LineEditText(Context context){
        this(context,null);
    }
    public LineEditText(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
    }
    private void init(Context context){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(R.color.line_edittext_underline_color));
    }

//    @Override
//    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//        return new DeleteInputConnection(super.onCreateInputConnection(outAttrs),
//                true);
//    }
//
//    private class DeleteInputConnection extends InputConnectionWrapper {
//
//        public DeleteInputConnection(InputConnection target, boolean mutable) {
//            super(target, mutable);
//        }
//
//        @Override
//        public boolean sendKeyEvent(KeyEvent event) {
//            return super.sendKeyEvent(event);
//        }
//
//        @Override
//        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
//            if (beforeLength == 1 && afterLength == 0) {
//                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
//                        KeyEvent.KEYCODE_DEL))
//                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
//                        KeyEvent.KEYCODE_DEL));
//            }
//
//            return super.deleteSurroundingText(beforeLength, afterLength);
//        }
//
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
