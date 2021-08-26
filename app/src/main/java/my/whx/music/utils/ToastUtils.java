package my.whx.music.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
public class ToastUtils {
    @SuppressLint("StaticFieldLeak")
    //声明context
    private static Context sContext;
    //声明toast
    private static Toast sToast;
    //在Application中初始化ToastUtils.init(this)
    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }
    //发送Toast,默认LENGTH_SHORT
    public static void show(int resId) {
        show(sContext.getString(resId));
    }
    //show方法
    public static void show(String text) {
        //如果sToast为空
        if (sToast == null) {
            //输出sToast信息
            sToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        } else {
            //设置sToast文本信息
            sToast.setText(text);
        }
        //调用sToast方法
        sToast.show();
    }
}
