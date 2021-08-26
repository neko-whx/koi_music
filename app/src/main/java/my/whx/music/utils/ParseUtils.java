package my.whx.music.utils;
@SuppressWarnings("ALL")
/**
 *解析工具类
 */
public class ParseUtils {
    //将字符串解析成整数的方法
    public static long parseInt(String s) {
        try {
            //返回解析后的整数值
            return Integer.parseInt(s);
            //输出异常信息
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    //将字符串解析成数字的方法
    public static long parseLong(String s) {
        try {
            //返回解析后的数字值
            return Long.parseLong(s);
            //输出异常信息
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
