package edu.nju.pm25.in.data.cache;

import com.xengine.android.data.cache.XBaseAdapterDBDataSource;
import com.xengine.android.data.db.XDBTable;
import edu.nju.pm25.in.data.db.CityListTable;
import edu.nju.pm25.in.data.model.CityBaseInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-24
 * Time: 下午9:00
 */
public class AllCitySource extends XBaseAdapterDBDataSource<CityBaseInfo> {
    @Override
    public String getSourceName() {
        return SourceName.ALL_CITY;
    }

    @Override
    public XDBTable<CityBaseInfo> getDatabaseTable() {
        return new CityListTable();
    }
}
