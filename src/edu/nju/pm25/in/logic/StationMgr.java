package edu.nju.pm25.in.logic;

import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.cache.StationBaseSource;
import edu.nju.pm25.in.data.cache.StationDetailSource;
import edu.nju.pm25.in.data.model.StationBaseInfo;
import edu.nju.pm25.in.data.model.StationDetailInfo;
import edu.nju.pm25.in.session.PM25Api;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:39
 */
public class StationMgr {
    private static StationMgr instance;

    public synchronized static StationMgr getInstance() {
        if(instance == null) {
            instance = new StationMgr();
        }
        return instance;
    }

    private StationBaseSource mStationBaseSource;
    private StationDetailSource mStationDetailSource;

    private StationMgr() {
        mStationBaseSource = (StationBaseSource) DataRepo.getInstance().getSource(SourceName.STATION_BASE);
        mStationDetailSource = (StationDetailSource) DataRepo.getInstance().getSource(SourceName.STATION_DETAIL);
    }

    /**
     * 刷新单个站点数据
     * @param stationCode
     * @return
     */
    public boolean refreshStation(String stationName, String stationCode) {
        boolean hasStation = true;
        StationDetailInfo stationInfo = mStationDetailSource.getById(stationName);
        if (stationInfo == null) {
            stationInfo = new StationDetailInfo();
            hasStation = false;
        }

        if (!PM25Api.getStationDetailInfo(stationCode, stationInfo))
            return false;

        if (hasStation)
            mStationDetailSource.notifyDataChanged();
        else
            mStationDetailSource.add(stationInfo);
        return true;
    }

    /**
     * 根据城市，刷新该城市的站点列表数据
     * @param cityName
     * @return
     */
    public boolean refreshStationList(String cityName) {
        List<StationBaseInfo> resultList = PM25Api.getStationBaseInfoList(cityName);
        if (resultList == null || resultList.size() == 0)
            return false;

        mStationBaseSource.addAll(resultList);
        mStationBaseSource.saveToDatabase();
        return true;
    }
}
