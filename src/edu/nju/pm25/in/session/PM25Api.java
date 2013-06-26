package edu.nju.pm25.in.session;

import com.xengine.android.utils.XLog;
import edu.nju.pm25.in.data.model.CityBaseInfo;
import edu.nju.pm25.in.data.model.CityDetailInfo;
import edu.nju.pm25.in.data.model.StationBaseInfo;
import edu.nju.pm25.in.data.model.StationDetailInfo;
import edu.nju.pm25.in.util.PMUtil;
import edu.nju.pm25.in.util.StringUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-18
 * Time: 下午4:49
 */
public class PM25Api {

    /**
     * 根据城市名，获得该城市的详细信息
     * @param city 城市名
     * @param result 结果
     * @return 该城市各个属性值
     * @throws IOException
     * @throws JSONException
     */
    public static boolean getCityDetailInfo(String city, CityDetailInfo result) {
        try {
            String url = PMUtil.getCityDetailInfo(city);
            HttpGet request = new HttpGet(url);
            HttpResponse response = HttpClientHolder.getMainHttpClient().execute(request, false);
            if (response == null)
                return false;

            String contentJson = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(contentJson);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            result.setCity(jsonObject.getString("area"));
            result.setAqi(String.valueOf(jsonObject.get("aqi")));
            result.setQuality(jsonObject.getString("quality"));
            result.setTime_point(jsonObject.getString("time_point"));
            result.setPm25(String.valueOf(jsonObject.get("pm2_5")));
            result.setPm10(String.valueOf(jsonObject.get("pm10")));
            result.setCo(String.valueOf(jsonObject.get("co")));
            result.setNo2(String.valueOf(jsonObject.get("no2")));
            result.setO3_1h(String.valueOf(jsonObject.get("o3")));
            result.setO3_8h(String.valueOf(jsonObject.get("o3_8h")));
            result.setSo2(String.valueOf(jsonObject.get("so2")));
            result.setLocalRefreshTime(System.currentTimeMillis());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得该城市所有监测点的基本信息
     * @param city
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<StationBaseInfo> getStationBaseInfoList(String city) {
        try {
            List<StationBaseInfo> stations = new ArrayList<StationBaseInfo>();
            String url = PMUtil.getAllStationsBaseInfo(city);
            HttpGet request = new HttpGet(url);
            HttpResponse response = HttpClientHolder.getMainHttpClient().execute(request, false);
            if (response == null)
                return null;

            String contentJson = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(contentJson);
            for (int i = 0; i < jsonArray.length() - 1; i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                StationBaseInfo station = new StationBaseInfo();
                station.setCity(jsonObject.getString("area"));
                station.setStation_code(jsonObject.getString("station_code"));
                station.setPosition_name(jsonObject.getString("position_name"));
                station.setAqi(String.valueOf(jsonObject.get("aqi")));
                station.setQuality(jsonObject.getString("quality"));
                station.setTime_point(jsonObject.getString("time_point"));
                stations.add(station);
            }
            return stations;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得某个检测点的详细信息
     * @param station_code
     * @return
     * @throws JSONException
     * @throws org.apache.http.ParseException
     * @throws IOException
     */
    public static boolean getStationDetailInfo(String station_code, StationDetailInfo result) {
        try {
            String url = PMUtil.getStationDetailInfo(station_code);
            HttpGet request = new HttpGet(url);
            HttpResponse response = HttpClientHolder.getMainHttpClient().execute(request, false);
            if (response == null)
                return false;

            String contentJson = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(contentJson);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            result.setCity(jsonObject.getString("area"));
            result.setName(jsonObject.getString("position_name"));
            result.setAqi(String.valueOf(jsonObject.get("aqi")));
            result.setQuality(jsonObject.getString("quality"));
            result.setTime_point(jsonObject.getString("time_point"));
            result.setPm25(String.valueOf(jsonObject.get("pm2_5")));
            result.setPm10(String.valueOf(jsonObject.get("pm10")));
            result.setCo(String.valueOf(jsonObject.get("co")));
            result.setNo2(String.valueOf(jsonObject.get("no2")));
            result.setO31h(String.valueOf(jsonObject.get("o3")));
            result.setO38h(String.valueOf(jsonObject.get("o3_8h")));
            result.setSo2(String.valueOf(jsonObject.get("so2")));
            result.setLocalRefreshTime(System.currentTimeMillis());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * 获得该城市所有监测点的基本信息
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<CityBaseInfo> getAllCityList() {
        try {
            List<CityBaseInfo> allCityList = new ArrayList<CityBaseInfo>();
            String url = PMUtil.getAllCityBaseInfo();
            HttpGet request = new HttpGet(url);
            HttpResponse response = HttpClientHolder.getMainHttpClient().execute(request, false);
            if (response == null)
                return null;

            String contentJson = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(contentJson);
            JSONArray cityArray = jsonObject.getJSONArray("cities");
            for (int i = 0; i < cityArray.length() - 1; i++) {
                CityBaseInfo cityBaseInfo = new CityBaseInfo();
                String cityChineseName = (String) cityArray.get(i);
                String pinyinName = StringUtil.toPinYin(cityChineseName);
                XLog.d("FK", cityChineseName + " ,拼音：" + pinyinName);
                cityBaseInfo.setChineseName(cityChineseName);
                cityBaseInfo.setPinyinName(pinyinName);
                allCityList.add(cityBaseInfo);
            }
            return allCityList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
