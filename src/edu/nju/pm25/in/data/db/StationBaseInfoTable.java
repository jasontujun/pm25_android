package edu.nju.pm25.in.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import com.xengine.android.data.db.XBaseDBTable;
import com.xengine.android.data.db.XSQLiteDataType;
import edu.nju.pm25.in.data.model.StationBaseInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:15
 */
public class StationBaseInfoTable extends XBaseDBTable<StationBaseInfo> {
    @Override
    public void initiateColumns() {
        addColumn("city", XSQLiteDataType.TEXT, null);
        addColumn("position_name", XSQLiteDataType.TEXT, null);
        addColumn("aqi", XSQLiteDataType.TEXT, null);
        addColumn("quality", XSQLiteDataType.TEXT, null);
        addColumn("time_point", XSQLiteDataType.TEXT, null);
        addColumn("station_code", XSQLiteDataType.TEXT, null);
        addColumn("refresh_time", XSQLiteDataType.LONG, null);
    }

    @Override
    public String getName() {
        return "station_base_info_table";
    }

    @Override
    public ContentValues getContentValues(StationBaseInfo instance) {
        ContentValues values = new ContentValues();
        values.put("city", instance.getCity());
        values.put("position_name", instance.getPosition_name());
        values.put("aqi", instance.getAqi());
        values.put("quality", instance.getQuality());
        values.put("time_point", instance.getTime_point());
        values.put("station_code", instance.getStation_code());
        values.put("refresh_time", instance.getLocalRefreshTime());
        return values;
    }

    @Override
    public StationBaseInfo getFilledInstance(Cursor cursor) {
        StationBaseInfo instance = new StationBaseInfo();
        instance.setCity(cursor.getString(cursor.getColumnIndex("city")));
        instance.setPosition_name(cursor.getString(cursor.getColumnIndex("position_name")));
        instance.setAqi(cursor.getString(cursor.getColumnIndex("aqi")));
        instance.setQuality(cursor.getString(cursor.getColumnIndex("quality")));
        instance.setTime_point(cursor.getString(cursor.getColumnIndex("time_point")));
        instance.setStation_code(cursor.getString(cursor.getColumnIndex("station_code")));
        instance.setLocalRefreshTime(cursor.getLong(cursor.getColumnIndex("refresh_time")));
        return instance;
    }
}
