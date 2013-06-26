package edu.nju.pm25.in.data.model;

public class StationBaseInfo {
	private String city;
	private String position_name;
	private String aqi;
	private String quality;
	private String time_point;
	private String station_code;

    private long localRefreshTime;// 本地更新时间
	
	public String getStation_code() {
		return station_code;
	}
	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}
	public String getPosition_name() {
		return position_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}
	public String getAqi() {
		return aqi;
	}
	public void setAqi(String aqi) {
		this.aqi = aqi;
	}
	public String getTime_point() {
		return time_point;
	}
	public void setTime_point(String time_point) {
		this.time_point = time_point;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

    public long getLocalRefreshTime() {
        return localRefreshTime;
    }

    public void setLocalRefreshTime(long localRefreshTime) {
        this.localRefreshTime = localRefreshTime;
    }
	
}
