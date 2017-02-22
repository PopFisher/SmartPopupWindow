package popup.popfisher.com.smartpopupwindow;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by popfisher on 2016/8/19.
 */

public class ScreenUtils {
    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }
}
