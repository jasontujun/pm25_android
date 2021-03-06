package edu.nju.pm25.in.data.model;

public class StationDetailInfo {
	private String city;
	private String name;
	private String aqi;
	private String time_point;
	private String quality;
	
	private String pm25;
	private String pm10;
	private String co;
	private String no2;
	private String o31h;
	private String o38h;
	private String so2;

    private long localRefreshTime;// 本地更新时间
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pM25) {
		pm25 = pM25;
	}
	public String getPm10() {
		return pm10;
	}
	public void setPm10(String pM10) {
		pm10 = pM10;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String cO) {
		co = cO;
	}
	public String getNo2() {
		return no2;
	}
	public void setNo2(String nO2) {
		no2 = nO2;
	}
	public String getO31h() {
		return o31h;
	}
	public void setO31h(String o31h) {
		this.o31h = o31h;
	}
	public String getO38h() {
		return o38h;
	}
	public void setO38h(String o38h) {
		this.o38h = o38h;
	}
	public String getSo2() {
		return so2;
	}
	public void setSo2(String sO2) {
		so2 = sO2;
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
