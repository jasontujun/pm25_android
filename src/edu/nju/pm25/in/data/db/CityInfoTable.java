package edu.nju.pm25.in.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import com.xengine.android.data.db.XBaseDBTable;
import com.xengine.android.data.db.XSQLiteDataType;
import edu.nju.pm25.in.data.model.CityDetailInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:15
 */
public class CityInfoTable extends XBaseDBTable<CityDetailInfo> {
    @Override
    public void initiateColumns() {
        addColumn("city", XSQLiteDataType.TEXT, null);
        addColumn("aqi", XSQLiteDataType.TEXT, null);
        addColumn("time_point", XSQLiteDataType.TEXT, null);
        addColumn("quality", XSQLiteDataType.TEXT, null);
        addColumn("pm25", XSQLiteDataType.TEXT, null);
        addColumn("pm10", XSQLiteDataType.TEXT, null);
        addColumn("co", XSQLiteDataType.TEXT, null);
        addColumn("no2", XSQLiteDataType.TEXT, null);
        addColumn("o3_1h", XSQLiteDataType.TEXT, null);
        addColumn("o3_8h", XSQLiteDataType.TEXT, null);
        addColumn("so2", XSQLiteDataType.TEXT, null);
        addColumn("refresh_time", XSQLiteDataType.LONG, null);
    }

    @Override
    public String getName() {
        return "city_info_table";
    }

    @Override
    public ContentValues getContentValues(CityDetailInfo instance) {
        ContentValues values = new ContentValues();
        values.put("city", instance.getCity());
        values.put("aqi", instance.getAqi());
        values.put("time_point", instance.getTime_point());
        values.put("quality", instance.getQuality());
        values.put("pm25", instance.getPm25());
        values.put("pm10", instance.getPm10());
        values.put("co", instance.getCo());
        values.put("no2", instance.getNo2());
        values.put("o3_1h", instance.getO3_1h());
        values.put("o3_8h", instance.getO3_8h());
        values.put("so2", instance.getSo2());
        values.put("refresh_time", instance.getLocalRefreshTime());
        return values;
    }

    @Override
    public CityDetailInfo getFilledInstance(Cursor cursor) {
        CityDetailInfo instance = new CityDetailInfo();
        instance.setCity(cursor.getString(cursor.getColumnIndex("city")));
        instance.setAqi(cursor.getString(cursor.getColumnIndex("aqi")));
        instance.setTime_point(cursor.getString(cursor.getColumnIndex("time_point")));
        instance.setQuality(cursor.getString(cursor.getColumnIndex("quality")));
        instance.setPm25(cursor.getString(cursor.getColumnIndex("pm25")));
        instance.setPm10(cursor.getString(cursor.getColumnIndex("pm10")));
        instance.setCo(cursor.getString(cursor.getColumnIndex("co")));
        instance.setNo2(cursor.getString(cursor.getColumnIndex("no2")));
        instance.setO3_1h(cursor.getString(cursor.getColumnIndex("o3_1h")));
        instance.setO3_8h(cursor.getString(cursor.getColumnIndex("o3_8h")));
        instance.setSo2(cursor.getString(cursor.getColumnIndex("so2")));
        instance.setLocalRefreshTime(cursor.getLong(cursor.getColumnIndex("refresh_time")));
        return instance;
    }
}
