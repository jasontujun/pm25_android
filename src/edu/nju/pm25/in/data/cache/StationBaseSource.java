package edu.nju.pm25.in.data.cache;

import com.xengine.android.data.cache.XBaseAdapterIdDBDataSource;
import com.xengine.android.data.db.XDBTable;
import edu.nju.pm25.in.data.db.StationBaseInfoTable;
import edu.nju.pm25.in.data.model.StationBaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午2:14
 */
public class StationBaseSource extends XBaseAdapterIdDBDataSource<StationBaseInfo> {
    @Override
    public String getSourceName() {
        return SourceName.STATION_BASE;
    }

    @Override
    public XDBTable<StationBaseInfo> getDatabaseTable() {
        return new StationBaseInfoTable();
    }

    public List<StationBaseInfo> getByCityName(String cityName) {
        List<StationBaseInfo> result = new ArrayList<StationBaseInfo>();
        for (int i = 0; i<size(); i++) {
            StationBaseInfo station = get(i);
            if (cityName.equals(station.getCity()))
                result.add(station);
        }
        return result;
    }

    @Override
    public String getId(StationBaseInfo stationBaseInfo) {
        return stationBaseInfo.getPosition_name();
    }
}
