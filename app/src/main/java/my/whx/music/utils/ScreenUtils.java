package my.whx.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.WindowManager;
@SuppressWarnings("ALL")
/**
 * 屏幕工具类
 */
public class ScreenUtils {
    @SuppressLint("StaticFieldLeak")
    //上下文对象
    private static Context sContext;
    //在Application中初始化ToastUtils.init(this)
    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }
    //获取屏幕宽度的方法
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = sContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //屏幕适配dp2px的方法
    public static int dp2px(float dpValue) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    //屏幕适配px2dp的方法
    public static int px2dp(float pxValue) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    //屏幕适配sp2px的方法
    public static int sp2px(float spValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
