package edu.nju.pm25.in.data.model;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-25
 * Time: 下午7:35
 */
public class CityBaseInfo {
    private String chineseName;
    private String pinyinName;

    public CityBaseInfo() {
    }

    public CityBaseInfo(String chineseName, String pinyinName) {
        this.chineseName = chineseName;
        this.pinyinName = pinyinName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }
}
