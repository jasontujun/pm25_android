package edu.nju.pm25.in.session;

import android.content.Context;
import com.xengine.android.session.http.XAndroidHttpClient;

/**
 * Created by jasontujun.
 * Date: 12-4-11
 * Time: 下午7:18
 */
public class HttpClientHolder {

    private static Context context;

    private static XAndroidHttpClient mainClient;// 主要的通信线程池

    private static XAndroidHttpClient imageClient;// 图片的通信线程池


    /**
     * 请使用getApplicationContext()来初始化
     */
    public static void init(Context c) {
        context = c;
    }

    public static synchronized XAndroidHttpClient getMainHttpClient() {
        if(mainClient == null) {
            mainClient = new XAndroidHttpClient(context);
        }
        return mainClient;
    }

    public static synchronized XAndroidHttpClient getImageHttpClient() {
        if(imageClient == null) {
            imageClient = new XAndroidHttpClient(context);
        }
        return mainClient;
    }
}
