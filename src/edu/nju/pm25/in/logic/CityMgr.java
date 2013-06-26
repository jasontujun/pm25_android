package edu.nju.pm25.in.logic;

import com.xengine.android.utils.XStringUtil;
import edu.nju.pm25.in.data.cache.*;
import edu.nju.pm25.in.data.model.CityBaseInfo;
import edu.nju.pm25.in.data.model.CityDetailInfo;
import edu.nju.pm25.in.session.PM25Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午4:38
 */
public class CityMgr {
    private static CityMgr instance;

    public synchronized static CityMgr getInstance() {
        if(instance == null) {
            instance = new CityMgr();
        }
        return instance;
    }

    public static final long UPDATE_CITY_INTERVAL = 24*60*60*1000;
    public static final long REFRESH_INTERVAL = 1*60*60*1000;
    private GlobalStateSource mGlobalStateSource;
    private FavoriteCitySource mFavoriteCitySource;
    private AllCitySource mAllCitySource;

    private CityMgr() {
        mGlobalStateSource = (GlobalStateSource) DataRepo.getInstance().getSource(SourceName.GLOBAL_STATE);
        mFavoriteCitySource = (FavoriteCitySource) DataRepo.getInstance().getSource(SourceName.FAVORITE_CITY);
        mAllCitySource = (AllCitySource) DataRepo.getInstance().getSource(SourceName.ALL_CITY);
    }

    /**
     * 添加城市
     * @param cityName
     * @return
     */
    public boolean addCity(String cityName) {
        // 如果已经存在，则返回失败
        if (mFavoriteCitySource.getIndexById(cityName) != -1)
            return false;

        CityDetailInfo cityDetailInfo = new CityDetailInfo();
        cityDetailInfo.setCity(cityName);

        mFavoriteCitySource.add(cityDetailInfo);
        mFavoriteCitySource.saveToDatabase();
        // 设置页码，显示刚添加的城市
        mGlobalStateSource.setSelectCityIndex(mFavoriteCitySource.size() - 1);
        return true;
    }

    /**
     * 删除城市
     * @param cityName
     */
    public void deleteCity(String cityName) {
        // 如果不存在，则返回失败
        if (mFavoriteCitySource.getIndexById(cityName) == -1)
            return;

        mFavoriteCitySource.deleteById(cityName);
        mFavoriteCitySource.saveToDatabase();
        // 设置页码，显示第一个城市
        if (mFavoriteCitySource.size() > 0) {
            mGlobalStateSource.setSelectCityIndex(0);
        }
    }

    /**
     * 刷新城市数据
     * @param cityName
     * @return
     */
    public boolean refreshCityData(String cityName) {
        boolean hasCity = true;
        CityDetailInfo cityDetailInfo = mFavoriteCitySource.getById(cityName);
        if (cityDetailInfo == null) {
            cityDetailInfo = new CityDetailInfo();
            hasCity = false;
        }

        if (!PM25Api.getCityDetailInfo(cityName, cityDetailInfo))
            return false;

        if (hasCity)
            mFavoriteCitySource.notifyDataChanged();
        else
            mFavoriteCitySource.add(cityDetailInfo);

        mFavoriteCitySource.saveToDatabase();

        return true;
    }

    /**
     * 更新所有城市的数据
     * @return
     */
    public boolean updateAllCityList() {
        List<CityBaseInfo> allCityList = PM25Api.getAllCityList();
        if (allCityList == null || allCityList.size() == 0)
            return false;

        if (mAllCitySource == null)
            return false;
        mAllCitySource.clear();
        mAllCitySource.addAll(allCityList);
        mAllCitySource.saveToDatabase();// 存储在本地数据库中
        mGlobalStateSource.setCityListUpdateTimeStamp(System.currentTimeMillis());
        return true;
    }


    /**
     * 根据用户输入过滤出符合条件的城市列表
     * @param input
     * @return
     */
    public List<CityBaseInfo> getFilteredCityList(String input) {
        List<CityBaseInfo> resultList = new ArrayList<CityBaseInfo>();
        // 将用户的输入转化为小写
        String inputLowerCase = input.toLowerCase();
        for (int i =0; i<mAllCitySource.size(); i++) {
            // 将版面的Id也转换为小写
            CityBaseInfo cityBaseInfo = mAllCitySource.get(i);
            if (!XStringUtil.isNullOrEmpty(cityBaseInfo.getPinyinName())) {
                String pinyin = cityBaseInfo.getPinyinName().toLowerCase();
                if (pinyin.contains(inputLowerCase) ||
                        cityBaseInfo.getChineseName().contains(inputLowerCase))
                    resultList.add(cityBaseInfo);
            } else {
                if (cityBaseInfo.getChineseName().contains(inputLowerCase))
                    resultList.add(cityBaseInfo);
            }
        }
        return resultList;
    }
}
