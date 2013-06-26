package edu.nju.pm25.in.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.xengine.android.utils.XLog;
import edu.nju.pm25.in.logic.CityMgr;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-25
 * Time: 下午9:10
 */
public class UpdateAllCityService extends Service {

    public static final String ACTION_BACKGROUND = "edu.nju.pm25.in.service.UpdateAllCity";
    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();

        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        XLog.d("SERVICE", "onStart service");

        if(intent == null) {
            return;
        }
        if (ACTION_BACKGROUND.equals(intent.getAction())) {
            startRequestBackService(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        XLog.d("SERVICE", "onStartCommand service");

        if(intent == null) {
            return START_NOT_STICKY;
        }
        if (ACTION_BACKGROUND.equals(intent.getAction())) {
            startRequestBackService(intent);
        }

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_NOT_STICKY;
    }


    /**
     * 启动轮询任务相关消息
     */
    private void startRequestBackService(Intent intent) {
        if(!isRunning) {
            XLog.d("SERVICE", "启动任务");
            UpdateAllCityTask updateAllCityTask = new UpdateAllCityTask();
            new Timer().schedule(updateAllCityTask, 0);
            isRunning = true;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class UpdateAllCityTask extends TimerTask {
        @Override
        public void run() {
            isRunning = true;
            if (CityMgr.getInstance().updateAllCityList()) {
                XLog.d("SERVICE", "城市数据更新成功！");
            } else {
                XLog.d("SERVICE", "城市数据更新失败！");
            }

            stopSelf();
        }
    }
}
