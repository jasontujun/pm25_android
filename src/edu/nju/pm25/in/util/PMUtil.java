package edu.nju.pm25.in.util;

import android.content.Context;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.model.CityBaseInfo;

import java.util.ArrayList;
import java.util.List;

public class PMUtil {

	private static String token = "efL4s6XPybHqT2PpJCQv";
	
	public static String getCityDetailInfo(String city) {
		return "http://www.pm25.in/api/querys/aqi_details.json?city=" + city + "&token=" + token + "&stations=no"; 
	}
	
	public static String getCityBaseInfo(String city) {
		return "http://www.pm25.in/api/querys/only_aqi.json?city=" + city + "&token=" + token + "&stations=no";
	}
	
	public static String getAllStationsBaseInfo(String city) {
		return "http://www.pm25.in/api/querys/only_aqi.json?city=" + city + "&token=" + token;
	}
	
	public static String getStationDetailInfo(String station_code) {
		return "http://www.pm25.in/api/querys/aqis_by_station.json?station_code=" + station_code + "&token=" + token;
	}

    public static String getAllCityBaseInfo() {
        return "http://www.pm25.in/api/querys.json?token=" + token;
    }
	
	public static String getHealthInfluenceByQuality(String quality) {
		if ("优".equals(quality)) {
            return "空气质量令人满意,基本无空气污染";
		} else if ("良".equals(quality)) {
            return "空气质量可接受,但某些污染物可能对极少数异常敏感人群健康有较弱影响";
		} else if ("轻度污染".equals(quality)){
            return "易感人群症状有轻度加剧,健康人群出现刺激症状";
		} else if ("中度污染".equals(quality)) {
            return "进一步加剧易感人群症状,可能对健康人群心脏、呼吸系统有影响";
		} else if ("重度污染".equals(quality)) {
            return "心脏病和肺病患者症状显著加剧,运动耐受力降低,健康人群普遍出现症状";
		} else if ("严重污染".equals(quality)) {
            return "健康人群运动耐受力降低,有明显 强烈症状,提前出现某些疾病";
        }
		return "无影响";
	}
	
	public static String getAdviceByQuality(String quality) {
		if ("优".equals(quality)) {
            return "各类人群可正常活动";
		} else if ("良".equals(quality)) {
            return "极少数异常敏感人群应减少户外活动";
		} else if ("轻度污染".equals(quality)){
            return"儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间、高强度的户外锻炼";
		} else if ("中度污染".equals(quality)) {
            return "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻练,一般人群适量减少户外运动";
		} else if ("重度污染".equals(quality)) {
            return "儿童、老年人和心脏病、肺病患者应停留在室内,停止户外运动,一般人群减少户外运动";
		} else if ("严重污染".equals(quality)) {
            return "儿童、老年人和病人应当留在室内,避免体力消耗,一般人群应避免户外活动";
        }
		return "无建议";
	}

    /**
     * 根据aqi空气指数获取背景图片
     * @param quality
     * @return 返回图片的资源id
     */
    public static int getBackgroundImageByQuality(String quality) {
        if ("优".equals(quality)) {
            return R.drawable.bg_aqi_level_1;
        } else if ("良".equals(quality)) {
            return R.drawable.bg_aqi_level_2;
        } else if ("轻度污染".equals(quality)) {
            return R.drawable.bg_aqi_level_3;
        } else if ("中度污染".equals(quality)) {
            return R.drawable.bg_aqi_level_4;
        } else if ("重度污染".equals(quality)) {
            return R.drawable.bg_aqi_level_5;
        } else if ("严重污染".equals(quality)) {
            return R.drawable.bg_aqi_level_6;
        } else {
            return R.drawable.bg_aqi_level_na;
        }
    }

    /**
     * 根据aqi空气指数获取对应文字的颜色
     * @param context
     * @param quality aqi空气指数
     * @return 返回对应的颜色值
     */
    public static int getColorByQuality(Context context, String quality) {
        if ("优".equals(quality)) {
            return context.getResources().getColor(R.color.light_green);
        } else if ("良".equals(quality)) {
            return context.getResources().getColor(R.color.light_blue);
        } else if ("轻度污染".equals(quality)) {
            return context.getResources().getColor(R.color.gray_green);
        } else if ("中度污染".equals(quality)) {
            return context.getResources().getColor(R.color.dark_yellow);
        } else if ("重度污染".equals(quality)) {
            return context.getResources().getColor(R.color.light_red);
        } else if ("严重污染".equals(quality)) {
            return context.getResources().getColor(R.color.light_purple);
        } else {
            return context.getResources().getColor(R.color.white);
        }
    }

    /**
     * 根据aqi空气指数获取对应图标
     * @param quality
     * @return 返回对应图标的资源id
     */
    public static int getIconByQuality(String quality) {
        if ("优".equals(quality)) {
            return R.drawable.icon_aqi_level_1;
        } else if ("良".equals(quality)) {
            return R.drawable.icon_aqi_level_2;
        } else if ("轻度污染".equals(quality)) {
            return R.drawable.icon_aqi_level_3;
        } else if ("中度污染".equals(quality)) {
            return R.drawable.icon_aqi_level_4;
        } else if ("重度污染".equals(quality)) {
            return R.drawable.icon_aqi_level_5;
        } else if ("严重污染".equals(quality)) {
            return R.drawable.icon_aqi_level_6;
        } else {
            return R.drawable.icon_aqi_level_na;
        }
    }

    /**
     * 默认城市：共76个
     * 上海 东莞 中山 丽水 乌鲁木齐 佛山 保定 兰州 北京 南京
     * 南宁 南昌 南通 厦门 台州 合肥 呼和浩特 哈尔滨 唐山 嘉兴
     * 大连 天津 太原 宁波 宿迁 常州 广州 廊坊 张家口 徐州
     * 惠州 成都 扬州 承德 拉萨 无锡 昆明 杭州 株洲 武汉
     * 江门 沈阳 沧州 泰州 济南 海口 淮安 深圳 温州 湖州
     * 湘潭 珠海 盐城 石家庄 福州 秦皇岛 绍兴 肇庆 舟山 苏州
     * 衡水 衢州 西宁 西安 贵阳 连云港 邢台 邯郸 郑州 重庆
     * 金华 银川 镇江 长春 长沙 青岛
     * @return
     */
    public static List<CityBaseInfo> getDefaultCityList() {
        List<CityBaseInfo> cityList = new ArrayList<CityBaseInfo>();
        cityList.add(new CityBaseInfo("上海", "shanghai"));
        cityList.add(new CityBaseInfo("东莞", "dongguan"));
        cityList.add(new CityBaseInfo("中山", "zhongshan"));
        cityList.add(new CityBaseInfo("丽水", "lishui"));
        cityList.add(new CityBaseInfo("乌鲁木齐", "wulumuqi"));
        cityList.add(new CityBaseInfo("佛山", "foshan"));
        cityList.add(new CityBaseInfo("保定", "baoding"));
        cityList.add(new CityBaseInfo("兰州", "lanzhou"));
        cityList.add(new CityBaseInfo("北京", "beijing"));
        cityList.add(new CityBaseInfo("南京", "nanjing"));
        cityList.add(new CityBaseInfo("南宁", "nanning"));
        cityList.add(new CityBaseInfo("南昌", "nanchang"));
        cityList.add(new CityBaseInfo("南通", "nantong"));
        cityList.add(new CityBaseInfo("厦门", "xiamen"));
        cityList.add(new CityBaseInfo("台州", "taizhou"));
        cityList.add(new CityBaseInfo("合肥", "hefei"));
        cityList.add(new CityBaseInfo("呼和浩特", "huhehaote"));
        cityList.add(new CityBaseInfo("哈尔滨", "haerbin"));
        cityList.add(new CityBaseInfo("唐山", "tangshan"));
        cityList.add(new CityBaseInfo("嘉兴", "jiaxing"));
        cityList.add(new CityBaseInfo("大连", "dalian"));
        cityList.add(new CityBaseInfo("天津", "tianjin"));
        cityList.add(new CityBaseInfo("太原", "taiyuan"));
        cityList.add(new CityBaseInfo("宁波", "ningbo"));
        cityList.add(new CityBaseInfo("宿迁", "suqian"));
        cityList.add(new CityBaseInfo("常州", "changzhou"));
        cityList.add(new CityBaseInfo("广州", "guangzhou"));
        cityList.add(new CityBaseInfo("廊坊", "langfang"));
        cityList.add(new CityBaseInfo("张家口", "zhangjiakou"));
        cityList.add(new CityBaseInfo("徐州", "xuzhou"));
        cityList.add(new CityBaseInfo("惠州", "huizhou"));
        cityList.add(new CityBaseInfo("成都", "chengdu"));
        cityList.add(new CityBaseInfo("扬州", "yangzhou"));
        cityList.add(new CityBaseInfo("承德", "chengde"));
        cityList.add(new CityBaseInfo("拉萨", "lasa"));
        cityList.add(new CityBaseInfo("无锡", "wuxi"));
        cityList.add(new CityBaseInfo("昆明", "kunming"));
        cityList.add(new CityBaseInfo("杭州", "hangzhou"));
        cityList.add(new CityBaseInfo("株洲", "zhuzhou"));
        cityList.add(new CityBaseInfo("武汉", "wuhan"));
        cityList.add(new CityBaseInfo("江门", "jiangmen"));
        cityList.add(new CityBaseInfo("沈阳", "shenyang"));
        cityList.add(new CityBaseInfo("沧州", "cangzhou"));
        cityList.add(new CityBaseInfo("泰州", "taizhou"));
        cityList.add(new CityBaseInfo("济南", "jinan"));
        cityList.add(new CityBaseInfo("海口", "haikou"));
        cityList.add(new CityBaseInfo("淮安", "huaian"));
        cityList.add(new CityBaseInfo("深圳", "shenzhen"));
        cityList.add(new CityBaseInfo("温州", "wenzhou"));
        cityList.add(new CityBaseInfo("湖州", "huzhou"));
        cityList.add(new CityBaseInfo("湘潭", "xiangtan"));
        cityList.add(new CityBaseInfo("珠海", "zhuhai"));
        cityList.add(new CityBaseInfo("盐城", "yancheng"));
        cityList.add(new CityBaseInfo("石家庄", "shijiazhuang"));
        cityList.add(new CityBaseInfo("福州", "fuzhou"));
        cityList.add(new CityBaseInfo("秦皇岛", "qinhuangdao"));
        cityList.add(new CityBaseInfo("绍兴", "shaoxing"));
        cityList.add(new CityBaseInfo("肇庆", "zhaoqing"));
        cityList.add(new CityBaseInfo("舟山", "zhoushan"));
        cityList.add(new CityBaseInfo("苏州", "suzhou"));
        cityList.add(new CityBaseInfo("衡水", "hengshui"));
        cityList.add(new CityBaseInfo("衢州", "quzhou"));
        cityList.add(new CityBaseInfo("西宁", "xining"));
        cityList.add(new CityBaseInfo("西安", "xian"));
        cityList.add(new CityBaseInfo("贵阳", "guiyang"));
        cityList.add(new CityBaseInfo("连云港", "lianyungang"));
        cityList.add(new CityBaseInfo("邢台", "xingtai"));
        cityList.add(new CityBaseInfo("邯郸", "handan"));
        cityList.add(new CityBaseInfo("郑州", "zhengzhou"));
        cityList.add(new CityBaseInfo("重庆", "chongqing"));
        cityList.add(new CityBaseInfo("金华", "jinhua"));
        cityList.add(new CityBaseInfo("银川", "yinchuan"));
        cityList.add(new CityBaseInfo("镇江", "zhenjiang"));
        cityList.add(new CityBaseInfo("长春", "changchun"));
        cityList.add(new CityBaseInfo("长沙", "changsha"));
        cityList.add(new CityBaseInfo("青岛", "qingdao"));
        return cityList;
    }
}
