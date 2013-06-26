package edu.nju.pm25.in.data.cache;

import android.content.Context;
import android.content.SharedPreferences;
import com.xengine.android.data.cache.XDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录全局都要用到的状态。
 * 存储在SharedPreferences中。
 * 包括当前登陆的用户名、密码和通信token。
 * Created by jasontujun.
 * Date: 12-2-23
 * Time: 下午3:34
 */
public class GlobalStateSource implements XDataSource {

    private static final String PREF_NAME = "pm25.globalState";

    private SharedPreferences pref;

    private static final String CITY_LIST_UPDATE_TIME_STAMP = "cityListUpdateTimeStamp";

    private static final String SELECT_CITY_INDEX = "selectCityIndex";

    private List<XCityIndexListener> mCityIndexListeners;

    /**
     * 全局状态数据源
     * @param context 请使用由getApplicationContext()获得的context
     */
    public GlobalStateSource(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mCityIndexListeners = new ArrayList<XCityIndexListener>();
    }

    @Override
    public String getSourceName() {
        return SourceName.GLOBAL_STATE;
    }

    public long getCityListUpdateTimeStamp() {
        return pref.getLong(CITY_LIST_UPDATE_TIME_STAMP, 0);
    }

    public void setCityListUpdateTimeStamp(long timeStamp) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(CITY_LIST_UPDATE_TIME_STAMP, timeStamp);
        editor.commit();
    }

    public int getSelectCityIndex() {
        return pref.getInt(SELECT_CITY_INDEX, -1);
    }

    public void setSelectCityIndex(int selectCityIndex) {
        int oldIndex = getSelectCityIndex();
        if (oldIndex == selectCityIndex)
            return;

        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(SELECT_CITY_INDEX, selectCityIndex);
        editor.commit();
        for (XCityIndexListener listener : mCityIndexListeners)
            listener.onChange(selectCityIndex);
    }

    public void registerSelectCityIndexListener(XCityIndexListener listener) {
        if (listener != null)
            mCityIndexListeners.add(listener);
    }

    public void unregisterSelectCityIndexListener(XCityIndexListener listener) {
        mCityIndexListeners.remove(listener);
    }

    /**
     * 当前显示城市的索引
     */
    public interface XCityIndexListener  {
        void onChange(int i);
    }
}
