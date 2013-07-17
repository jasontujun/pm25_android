package edu.nju.pm25.in.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.xengine.android.data.cache.XDataChangeListener;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.FavoriteCitySource;
import edu.nju.pm25.in.data.cache.GlobalStateSource;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.model.CityDetailInfo;
import edu.nju.pm25.in.logic.CityMgr;
import edu.nju.pm25.in.logic.StationMgr;
import edu.nju.pm25.in.logic.SystemMgr;
import edu.nju.pm25.in.service.UpdateAllCityService;
import edu.nju.pm25.in.ui.controls.viewflow.CircleFlowIndicator;
import edu.nju.pm25.in.ui.controls.viewflow.ViewFlow;
import edu.nju.pm25.in.util.PMUtil;

import java.util.List;

/**
 * 主界面。显示关注的城市环境数据。
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-18
 * Time: 下午4:43
 */
public class CityInfoActivity extends Activity
        implements XDataChangeListener<CityDetailInfo>, GlobalStateSource.XCityIndexListener {

    private GlobalStateSource mGlobalStateSource;
    private FavoriteCitySource mFavoriteCitySource;

    private ImageSwitcher mBackground;
    private ImageButton mCityMgrBtn;
    private ImageButton mRefreshBtn;
    private TextView mCityNameView;
    private TextView mNothingTipView;
    private ViewFlow mCityViewFlow;
    private CircleFlowIndicator mCityIndicator;
    private CityViewFlowAdapter mCityViewFlowAdapter;

    private CityDetailInfo mCurrentCity;// 当前选择的城市
    private boolean mIsRefreshing;
    private RefreshDataTask mRefreshDataTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化系统相关组件
        SystemMgr.getInstance().initSystem(getApplicationContext());

        // 初始化数据源
        mGlobalStateSource = (GlobalStateSource) DataRepo.
                getInstance().getSource(SourceName.GLOBAL_STATE);
        mFavoriteCitySource = (FavoriteCitySource) DataRepo.
                getInstance().getSource(SourceName.FAVORITE_CITY);
        mFavoriteCitySource.registerDataChangeListener(this);
        mGlobalStateSource.registerSelectCityIndexListener(this);

        // ui
        setContentView(R.layout.frame_city_info);
        mBackground = (ImageSwitcher) findViewById(R.id.bg);
        mCityMgrBtn = (ImageButton) findViewById(R.id.add_btn);
        mRefreshBtn = (ImageButton) findViewById(R.id.refresh_btn);
        mCityNameView = (TextView) findViewById(R.id.city_name_txt);
        mNothingTipView = (TextView) findViewById(R.id.nothing_tip);
        mCityViewFlow = (ViewFlow) findViewById(R.id.city_viewflow);
        mCityIndicator = (CircleFlowIndicator) findViewById(R.id.city_indic);

        // 初始化background的渐变切换
        mBackground.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView i = new ImageView(CityInfoActivity.this);
                i.setScaleType(ImageView.ScaleType.FIT_XY);
                i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                return i;
            }
        });
        mBackground.setInAnimation(AnimationUtils.loadAnimation(CityInfoActivity.this,
                android.R.anim.fade_in));
        mBackground.setOutAnimation(AnimationUtils.loadAnimation(CityInfoActivity.this,
                android.R.anim.fade_out));

        // 初始化按钮监听
        mCityMgrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CityInfoActivity.this, CityMgrActivity.class);
                startActivity(intent);
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
                    startRefreshTask(mCurrentCity.getCity(), true);
                }
            }
        });

        // 初始化城市列表
        mCityViewFlowAdapter = new CityViewFlowAdapter(CityInfoActivity.this);
        mCityViewFlow.setAdapter(mCityViewFlowAdapter);
        mCityViewFlow.setFlowIndicator(mCityIndicator);
        mFavoriteCitySource.registerDataChangeListener(mCityViewFlowAdapter);// 添加viewflow对数据源的监听

        // TODO 更新城市列表数据
        if (System.currentTimeMillis() - mGlobalStateSource.getCityListUpdateTimeStamp()
                > CityMgr.UPDATE_CITY_INTERVAL) {
            Intent intent = new Intent(UpdateAllCityService.ACTION_BACKGROUND);
            intent.setClass(CityInfoActivity.this, UpdateAllCityService.class);
            startService(intent);
        }
    }

    private void startRefreshTask(String cityName, boolean hasTip) {
        cancelRefreshTask();
        mIsRefreshing = true;
        mRefreshDataTask = new RefreshDataTask(cityName, hasTip);
        mRefreshDataTask.execute(null);
    }

    private void cancelRefreshTask() {
        mIsRefreshing = false;
        if (mRefreshDataTask != null) {
            mRefreshDataTask.cancel(true);
            mRefreshDataTask = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        refresh();
        // 设置滑动监听(在viewflow初始化完后添加)
        mCityViewFlow.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {
            @Override
            public void onSwitched(View view, int position) {
                if (mIsRefreshing)
                    cancelRefreshTask();

                // 将当前选择的城市序号存入SharePreference，并通知监听者，刷新界面
                mGlobalStateSource.setSelectCityIndex(position);

                // 判断是否要刷新(如果间隔时间过大，则自动刷新)
                CityDetailInfo cityDetailInfo = mFavoriteCitySource.get(position);
                if (System.currentTimeMillis() - cityDetailInfo.getLocalRefreshTime()
                        > CityMgr.REFRESH_INTERVAL)
                    startRefreshTask(cityDetailInfo.getCity(), false);
            }
        });
    }

    /**
     * 当关注的城市数量发生变化时，刷新界面
     */
    private void refresh() {
        if (mFavoriteCitySource.size() == 0) {
            // 还未添加过城市
            mBackground.setVisibility(View.GONE);
            mCityNameView.setVisibility(View.GONE);
            mRefreshBtn.setVisibility(View.GONE);
            mCityViewFlow.setVisibility(View.GONE);
            mCityIndicator.setVisibility(View.GONE);
            mNothingTipView.setVisibility(View.VISIBLE);
        } else {
            mBackground.setVisibility(View.VISIBLE);
            mCityNameView.setVisibility(View.VISIBLE);
            mRefreshBtn.setVisibility(View.VISIBLE);
            mCityViewFlow.setVisibility(View.VISIBLE);
            mCityIndicator.setVisibility(View.VISIBLE);
            mNothingTipView.setVisibility(View.GONE);

            // 更新城市列表
            int selectedIndex = mGlobalStateSource.getSelectCityIndex();
            CityDetailInfo cityDetailInfo = mFavoriteCitySource.get(selectedIndex);
            if (cityDetailInfo == null)
                return;

            // 设置背景图片、城市名称
            mCurrentCity = cityDetailInfo;
            mCityNameView.setText(mCurrentCity.getCity());
            mBackground.setImageResource(PMUtil.getBackgroundImageByQuality(mCurrentCity.getQuality()));
            // 如果当前viewflow不是显示对应的页码，则滑动到对应页码
            if (mCityViewFlow.getSelectedItemPosition() != selectedIndex) {
                mCityViewFlow.setSelection(selectedIndex);
            } else {
                // 判断是否要刷新(如果间隔时间过大，则自动刷新)
                if (System.currentTimeMillis() - mCurrentCity.getLocalRefreshTime()
                        > CityMgr.REFRESH_INTERVAL) {
                    if (mIsRefreshing)
                        cancelRefreshTask();
                    startRefreshTask(cityDetailInfo.getCity(), false);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止后台的更新任务AsyncTask
        if (mIsRefreshing)
            cancelRefreshTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFavoriteCitySource.unregisterDataChangeListener(mCityViewFlowAdapter);
        mFavoriteCitySource.unregisterDataChangeListener(this);
        mGlobalStateSource.unregisterSelectCityIndexListener(this);
        SystemMgr.getInstance().clearSystem();
    }


    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onChange() {
        postRefresh();
    }

    @Override
    public void onAdd(CityDetailInfo cityDetailInfo) {
        postRefresh();
    }

    @Override
    public void onAddAll(List<CityDetailInfo> cityDetailInfos) {
        postRefresh();
    }

    @Override
    public void onDelete(CityDetailInfo cityDetailInfo) {
        postRefresh();
    }

    @Override
    public void onDeleteAll(List<CityDetailInfo> cityDetailInfos) {
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
     * 当前城市的切换
     * @param i
     */
    @Override
    public void onChange(int i) {
        postRefresh();
    }


    /**
     * 刷新某城市的环境数据的异步线程
     */
    private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        private boolean mHasTips;
        private String mCityName;
        private boolean mResult;

        private RefreshDataTask(String cityName, boolean hasTips) {
            mCityName = cityName;
            mHasTips = hasTips;
        }

        @Override
        protected void onPreExecute() {
            Animation rotate = AnimationUtils.loadAnimation(CityInfoActivity.this, R.anim.rotate);
            mRefreshBtn.startAnimation(rotate);
            if (mHasTips)
                Toast.makeText(CityInfoActivity.this, "正在更新数据...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... para) {
            StationMgr.getInstance().refreshStationList(mCityName);// 刷新当前城市的所有站点列表数据
            mResult = CityMgr.getInstance().refreshCityData(mCityName);// 刷新当前城市的综合数据
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mHasTips) {
                if (mResult)
                    Toast.makeText(CityInfoActivity.this, "数据更新成功！", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CityInfoActivity.this, "数据更新失败，请检查网络状况~", Toast.LENGTH_SHORT).show();
            }

            mRefreshBtn.clearAnimation();
            mIsRefreshing = false;
        }

        @Override
        protected void onCancelled() {
            mRefreshBtn.clearAnimation();

            if (mHasTips)
                Toast.makeText(CityInfoActivity.this, "您取消了更新~", Toast.LENGTH_SHORT).show();
        }
    }
}
