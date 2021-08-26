package my.whx.music.utils;

import android.view.View;

import my.whx.music.enums.LoadStateEnum;

/**
 * 视图工具类
 */
public class ViewUtils {
    public static void changeViewState(View success, View loading, View fail, LoadStateEnum state) {
        switch (state) {
            //正在加载
            case LOADING:
                //设置成功时可见度为不可见
                success.setVisibility(View.GONE);
                //设置加载时可见度为可见
                loading.setVisibility(View.VISIBLE);
                //设置失败时可见度为不可见
                fail.setVisibility(View.GONE);
                break;
            //加载成功
            case LOAD_SUCCESS:
                //设置成功时可见度为可见
                success.setVisibility(View.VISIBLE);
                //设置加载时可见度为不可见
                loading.setVisibility(View.GONE);
                //设置失败时可见度为不可见
                fail.setVisibility(View.GONE);
                break;
            //加载失败
            case LOAD_FAIL:
                //设置成功时可见度为不可见
                success.setVisibility(View.GONE);
                //设置加载时可见度为不可见
                loading.setVisibility(View.GONE);
                //设置失败时可见度为可见
                fail.setVisibility(View.VISIBLE);
                break;
        }
    }
}
