package edu.nju.pm25.in.data.cache;

import com.xengine.android.data.cache.XBaseAdapterIdDataSource;
import edu.nju.pm25.in.data.model.StationDetailInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:20
 */
public class StationDetailSource extends XBaseAdapterIdDataSource<StationDetailInfo> {
    @Override
    public String getSourceName() {
        return SourceName.STATION_DETAIL;
    }

    @Override
    public String getId(StationDetailInfo stationDetailInfo) {
        return stationDetailInfo.getName();
    }
}
