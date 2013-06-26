package edu.nju.pm25.in.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.xengine.android.data.cache.XDataChangeListener;
import com.xengine.android.utils.XLog;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.cache.StationDetailSource;
import edu.nju.pm25.in.data.model.StationDetailInfo;
import edu.nju.pm25.in.logic.CityMgr;
import edu.nju.pm25.in.logic.StationMgr;
import edu.nju.pm25.in.util.PMUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-24
 * Time: 下午4:51
 */
public class StationInfoActivity extends Activity implements XDataChangeListener<StationDetailInfo> {

    private StationDetailSource mStationDetailSource;

    private TextView mNothingTipView;
    private View mStationInfoFrame;
    private TextView mStationNameView;
    private ImageButton mBackBtn;
    private ImageButton mRefreshBtn;
    private TextView mAqiView;
    private TextView mAqiLevelView;
    private TextView mPm25View;
    private TextView mPm10View;
    private TextView mCoView;
    private TextView mNo2View;
    private TextView mO31hView;
    private TextView mO38hView;
    private TextView mSo2View;
    private TextView mInfluenceView;
    private TextView mAdviceView;

    private String mCityName;
    private String mStationName;
    private String mStationCode;
    private boolean mIsRefreshing;
    private RefreshDataTask mRefreshDataTask;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据源
        mStationDetailSource = (StationDetailSource) DataRepo.
                getInstance().getSource(SourceName.STATION_DETAIL);
        mStationDetailSource.registerDataChangeListener(this);

        Intent intent = getIntent();
        mCityName = intent.getStringExtra("cityName");
        mStationName = intent.getStringExtra("stationName");
        mStationCode = intent.getStringExtra("stationCode");
        XLog.d("FK", mStationName + " stationCode:" + mStationCode);

        // ui
        setContentView(R.layout.frame_station_info);
        mStationInfoFrame = findViewById(R.id.station_info_frame);
        mNothingTipView = (TextView) findViewById(R.id.nothing_tip);
        mStationNameView = (TextView) findViewById(R.id.station_name);
        mBackBtn = (ImageButton) findViewById(R.id.back_btn);
        mRefreshBtn = (ImageButton) findViewById(R.id.refresh_btn);
        mAqiView = (TextView) findViewById(R.id.aqi);
        mAqiLevelView = (TextView) findViewById(R.id.aqi_level);
        mPm25View = (TextView) findViewById(R.id.pm25);
        mPm10View = (TextView) findViewById(R.id.pm10);
        mCoView = (TextView) findViewById(R.id.co);
        mNo2View = (TextView) findViewById(R.id.no2);
        mO31hView = (TextView) findViewById(R.id.o3_1h);
        mO38hView = (TextView) findViewById(R.id.o3_8h);
        mSo2View = (TextView) findViewById(R.id.so2);
        mInfluenceView = (TextView) findViewById(R.id.influence);
        mAdviceView = (TextView) findViewById(R.id.advice);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StationInfoActivity.this.finish();
            }
        });
        mIsRefreshing = false;
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 启动刷新或终止刷新
                if (mIsRefreshing) {
                    cancelRefreshTask();
                } else {
                    startRefreshTask(true);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        refresh();

        // 如果不存在该站点的详细数据，则加载数据
        StationDetailInfo stationDetailInfo = mStationDetailSource.getById(mStationName);
        if (stationDetailInfo == null) {
            startRefreshTask(true);
            return;
        }

        if (System.currentTimeMillis() - stationDetailInfo.getLocalRefreshTime() > CityMgr.REFRESH_INTERVAL) {
            startRefreshTask(true);
            return;
        }
    }

    private void refresh() {
        mStationNameView.setText(mCityName + "-" + mStationName);

        StationDetailInfo stationDetailInfo = mStationDetailSource.getById(mStationName);
        if (stationDetailInfo == null) {
            mNothingTipView.setVisibility(View.VISIBLE);
            mStationInfoFrame.setVisibility(View.GONE);
        } else {
            mNothingTipView.setVisibility(View.GONE);
            mStationInfoFrame.setVisibility(View.VISIBLE);

            mAqiView.setText(stationDetailInfo.getAqi());
            String quality = stationDetailInfo.getQuality();
            mAqiLevelView.setText(quality);
            mAqiLevelView.setTextColor(PMUtil.getColorByQuality(StationInfoActivity.this, quality));
            mStationInfoFrame.setBackgroundResource(PMUtil.getBackgroundImageByQuality(quality));
            mInfluenceView.setText("健康影响：" + PMUtil.getHealthInfluenceByQuality(quality));
            mAdviceView.setText("温馨提醒：" + PMUtil.getAdviceByQuality(quality));

            mPm25View.setText("PM2.5 细颗粒物：" + stationDetailInfo.getPm25());
            mPm10View.setText("PM10 可吸入颗粒物：" + stationDetailInfo.getPm10());
            mCoView.setText("CO 一氧化碳：" + stationDetailInfo.getCo());
            mNo2View.setText("NO2 二氧化氮：" + stationDetailInfo.getNo2());
            mO31hView.setText("O3 臭氧一小时平均值：" + stationDetailInfo.getO31h());
            mO38hView.setText("O3 臭氧八小时平均值：" + stationDetailInfo.getO38h());
            mSo2View.setText("SO2 二氧化硫：" + stationDetailInfo.getSo2());
        }
    }

    private void startRefreshTask(boolean hasTip) {
        cancelRefreshTask();
        mRefreshDataTask = new RefreshDataTask(mStationCode, hasTip);
        mRefreshDataTask.execute(null);
    }

    private void cancelRefreshTask() {
        if (mRefreshDataTask != null) {
            mRefreshDataTask.cancel(true);
            mRefreshDataTask = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止后台的更新任务AsyncTask
        if (mIsRefreshing) {
            cancelRefreshTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStationDetailSource.unregisterDataChangeListener(this);
    }

    @Override
    public void onChange() {
        postRefresh();
    }

    @Override
    public void onAdd(StationDetailInfo stationDetailInfo) {
        postRefresh();
    }

    @Override
    public void onAddAll(List<StationDetailInfo> stationDetailInfos) {
        postRefresh();
    }

    @Override
    public void onDelete(StationDetailInfo stationDetailInfo) {
        postRefresh();
    }

    @Override
    public void onDeleteAll(List<StationDetailInfo> stationDetailInfos) {
        postRefresh();
    }

    private void postRefresh() {
        changeHandler.sendEmptyMessage(0);
    }

    private Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                refresh();
            }
        }
    };

    /**
     * 刷新某站点的详细数据的异步线程
     */
    private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        private boolean mHasTips;
        private String mStationCode;
        private boolean mResult;

        private RefreshDataTask(String cityName, boolean hasTips) {
            mStationCode = cityName;
            mHasTips = hasTips;
        }

        @Override
        protected void onPreExecute() {
            mIsRefreshing = true;
            Animation rotate = AnimationUtils.loadAnimation(StationInfoActivity.this, R.anim.rotate);
            mRefreshBtn.startAnimation(rotate);
            if (mHasTips)
                Toast.makeText(StationInfoActivity.this, "正在更新数据...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... para) {
            mResult = StationMgr.getInstance().refreshStation(mStationName, mStationCode);// 刷新当前站点的详细数据
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mHasTips) {
                if (mResult)
                    Toast.makeText(StationInfoActivity.this, "数据更新成功！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StationInfoActivity.this, "数据更新失败，请检查网络状况~", Toast.LENGTH_SHORT).show();
            }

            mRefreshBtn.clearAnimation();
            mIsRefreshing = false;
        }

        @Override
        protected void onCancelled() {
            mRefreshBtn.clearAnimation();

            if (mHasTips)
                Toast.makeText(StationInfoActivity.this, "您取消了更新~", Toast.LENGTH_SHORT).show();

            mIsRefreshing = false;
        }
    }
}
