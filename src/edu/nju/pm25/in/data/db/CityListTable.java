package edu.nju.pm25.in.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import com.xengine.android.data.db.XBaseDBTable;
import com.xengine.android.data.db.XSQLiteDataType;
import edu.nju.pm25.in.data.model.CityBaseInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:15
 */
public class CityListTable extends XBaseDBTable<CityBaseInfo> {
    @Override
    public void initiateColumns() {
        addColumn("chinese_name", XSQLiteDataType.TEXT, null);
        addColumn("pinyin_name", XSQLiteDataType.TEXT, null);
    }

    @Override
    public String getName() {
        return "city_list_table";
    }

    @Override
    public ContentValues getContentValues(CityBaseInfo instance) {
        ContentValues values = new ContentValues();
        values.put("chinese_name", instance.getChineseName());
        values.put("pinyin_name", instance.getPinyinName());
        return values;
    }

    @Override
    public CityBaseInfo getFilledInstance(Cursor cursor) {
        CityBaseInfo instance = new CityBaseInfo();
        instance.setChineseName(cursor.getString(cursor.getColumnIndex("chinese_name")));
        instance.setPinyinName(cursor.getString(cursor.getColumnIndex("pinyin_name")));
        return instance;
    }
}
