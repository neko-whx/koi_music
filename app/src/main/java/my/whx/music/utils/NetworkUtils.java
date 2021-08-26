package my.whx.music.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
@SuppressWarnings("ALL")
/**
 * 网络工具类
 */
public class NetworkUtils {
    //网络是否可用的方法
    public static boolean isNetworkAvailable(Context context) {
        //判断当前网络连接状态
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断连接管理器是否为空
        if (connectivityManager != null) {
            //设置连接管理器来获取网络信息
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            //判断网络信息状态是否为空
            if (allNetworkInfo != null) {
                //遍历每一条网络信息
                for (NetworkInfo networkInfo : allNetworkInfo) {
                    //判断网络信息状态是否为连接
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //移动热点是否可用的方法
    public static boolean isActiveNetworkMobile(Context context) {
        //判断移动热点连接状态
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断连接管理器是否为空
        if (connectivityManager != null) {
            //设置连接管理器来获取网络信息
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //判断移动热点的网络信息是否为空
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
    //wifi是否可用的方法
    public static boolean isActiveNetworkWifi(Context context) {
        //判断wifi连接状态
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //判断连接管理器是否为空
        if (connectivityManager != null) {
            //设置连接管理器来获取网络信息
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //判断wifi的网络信息是否为空
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
