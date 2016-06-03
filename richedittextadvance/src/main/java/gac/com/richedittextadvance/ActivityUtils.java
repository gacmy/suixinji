package gac.com.richedittextadvance;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by gacmy on 2016/5/31.
 */
public class ActivityUtils {
    public static String[] getArray(Context context,int id){
        if(context == null){
            return null;
        }
       return context.getResources().getStringArray(id);

    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        //int height = wm.getDefaultDisplay().getHeight();
        return width;
    }
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        //int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
}
