package edu.nju.pm25.in.logic;

import android.content.Context;
import com.xengine.android.data.db.XSQLiteHelper;
import com.xengine.android.system.file.XAndroidFileMgr;
import com.xengine.android.system.file.XFileMgr;
import edu.nju.pm25.in.data.cache.*;
import edu.nju.pm25.in.session.HttpClientHolder;
import edu.nju.pm25.in.util.PMUtil;

import java.io.File;

/**
 * Created by jasontujun.
 * Date: 12-4-27
 * Time: 下午3:08
 */
public class SystemMgr {
    private static SystemMgr instance;

    public synchronized static SystemMgr getInstance() {
        if(instance == null) {
            instance = new SystemMgr();
        }
        return instance;
    }

    private SystemMgr() {}


    public static final int DIR_DATA_IMG = 20;
    public static final int DIR_DATA_SPEECH = 21;


    /**
     * 在第一个Activity加载时执行
     * @param context
     */
    public void preInit(Context context) {
        // 初始化文件管理器
        XFileMgr fileMgr = XAndroidFileMgr.getInstance();
        fileMgr.setRootName("pm25");
        fileMgr.setDir(XFileMgr.FILE_TYPE_TMP, "tmp", true);
        fileMgr.setDir(XFileMgr.FILE_TYPE_PHOTO, "photo", true);
        // 初始化网络
        HttpClientHolder.init(context);
        // 初始化手机功能管理器
//        XScreen screen = new XAndroidScreen(context);
//        mMobileMgr = new XAndroidMobileMgr(this, screen.getScreenWidth(), screen.getScreenHeight());
    }


    /**
     * 初始化系统。
     * @param context
     */
    public void initSystem(Context context) {

        clearSystem();
        initFileMgr();
        initDB(context);
        initDataSources(context);
    }

    private void initFileMgr() {
        // 文件夹管理器
        XFileMgr fileMgr = XAndroidFileMgr.getInstance();
        fileMgr.setDir(DIR_DATA_IMG, "data" + File.separator + "image", false);
        fileMgr.setDir(DIR_DATA_SPEECH, "data" + File.separator + "speech", false);
        // 图片下载管理器
//        ImgMgrHolder.getImgDownloadMgr().setDownloadDirectory(
//                fileMgr.getDir(XFileMgr.FILE_TYPE_TMP).getAbsolutePath());
    }

    /**
     * 初始化数据库
     * @param context
     */
    private void initDB(Context context) {
        // 初始化数据库
        XSQLiteHelper.initiate(context, "pm25_db", 1);
    }

    /**
     * 初始化数据源。
     * 一部分是空数据源。
     * 一部分从sharePreference导入。
     * 一部分从SQLite导入。
     */
    private void initDataSources(Context context) {
        DataRepo repo = DataRepo.getInstance();

        repo.registerDataSource(new GlobalStateSource(context));

        AllCitySource allCitySource = new AllCitySource();
        allCitySource.loadFromDatabase();
        if (allCitySource.size() == 0) {
            // 如果当前本地数据为空，则加载默认城市列表
            allCitySource.addAll(PMUtil.getDefaultCityList());
        }
        repo.registerDataSource(allCitySource);

        FavoriteCitySource favoriteCitySource = new FavoriteCitySource();
        favoriteCitySource.loadFromDatabase();// 从数据库加载数据
        repo.registerDataSource(favoriteCitySource);

        StationBaseSource stationBaseSource = new StationBaseSource();
        stationBaseSource.loadFromDatabase();// 从数据库加载数据
        repo.registerDataSource(stationBaseSource);

        repo.registerDataSource(new StationDetailSource());
    }

    public void clearSystem() {
        // clear image cache
//        ImageLoader.getInstance().clearImageCache();

        // clear tmp file
        XAndroidFileMgr.getInstance().clearDir(XFileMgr.FILE_TYPE_TMP);
        XAndroidFileMgr.getInstance().clearDir(XFileMgr.FILE_TYPE_PHOTO);

        // clear Mgr
//        LoginMgr.clearInstance();
//        BbsArticleMgr.clearInstance();
//        BbsBoardMgr.clearInstance();
//        BbsMailMgr.clearInstance();
//        BbsPersonMgr.clearInstance();

        // clear DataSource
        DataRepo.clearInstance();
    }
}
