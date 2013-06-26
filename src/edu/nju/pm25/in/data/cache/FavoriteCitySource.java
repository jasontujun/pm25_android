package edu.nju.pm25.in.data.cache;

import com.xengine.android.data.cache.XBaseAdapterIdDBDataSource;
import com.xengine.android.data.db.XDBTable;
import edu.nju.pm25.in.data.db.CityInfoTable;
import edu.nju.pm25.in.data.model.CityDetailInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-18
 * Time: 下午4:54
 */
public class FavoriteCitySource extends XBaseAdapterIdDBDataSource<CityDetailInfo> {
    @Override
    public String getSourceName() {
        return SourceName.FAVORITE_CITY;
    }

    @Override
    public XDBTable<CityDetailInfo> getDatabaseTable() {
        return new CityInfoTable();
    }

    @Override
    public String getId(CityDetailInfo cityDetailInfo) {
        return cityDetailInfo.getCity();
    }
}
