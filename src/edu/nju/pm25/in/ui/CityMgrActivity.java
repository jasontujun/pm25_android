package edu.nju.pm25.in.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import com.xengine.android.data.cache.XDataChangeListener;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.FavoriteCitySource;
import edu.nju.pm25.in.data.cache.GlobalStateSource;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.model.CityDetailInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午12:21
 */
public class CityMgrActivity extends Activity implements XDataChangeListener<CityDetailInfo> {

    private ImageButton mBackBtn;
    private ImageButton mEditBtn;
    private GridView mCityGridView;
    private CityGridViewAdapter mCityAdapter;

    private int mode;
    private static final int UI_MODE_NORMAL = 0;
    private static final int UI_MODE_EDIT = 1;

    private GlobalStateSource mGlobalStateSource;
    private FavoriteCitySource mFavoriteCitySource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据源
        mGlobalStateSource = (GlobalStateSource) DataRepo.
                getInstance().getSource(SourceName.GLOBAL_STATE);
        mFavoriteCitySource = (FavoriteCitySource) DataRepo.
                getInstance().getSource(SourceName.FAVORITE_CITY);
        mFavoriteCitySource.registerDataChangeListener(this);

        // ui
        setContentView(R.layout.frame_city_manager);
        mBackBtn = (ImageButton) findViewById(R.id.back_btn);
        mEditBtn = (ImageButton) findViewById(R.id.edit_btn);
        mCityGridView = (GridView) findViewById(R.id.city_gridview);

        // 初始化按钮监听
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityMgrActivity.this.finish();
            }
        });
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 编辑模式
                if (mode == UI_MODE_NORMAL) {
                    mode = UI_MODE_EDIT;
                    modeRefresh();
                } else {
                    mode = UI_MODE_NORMAL;
                    modeRefresh();
                }
            }
        });

        // 初始化城市列表
        mCityAdapter = new CityGridViewAdapter(CityMgrActivity.this);
        mCityGridView.setAdapter(mCityAdapter);

        // 注册对数据源的监听
        mFavoriteCitySource.registerDataChangeListener(mCityAdapter);
        mGlobalStateSource.registerSelectCityIndexListener(mCityAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mode = UI_MODE_NORMAL;
        refresh();
    }


    /**
     * 当关注的城市数量发生变化时，刷新界面
     */
    private void refresh() {
        if (mode == UI_MODE_NORMAL) {
            if (mFavoriteCitySource.size() == 0) {
                // 还未添加过城市
                mEditBtn.setVisibility(View.GONE);
            } else {
                mEditBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void modeRefresh() {
        if (mode == UI_MODE_NORMAL) {
            mBackBtn.setVisibility(View.VISIBLE);
            mEditBtn.setImageResource(R.drawable.btn_city_manager_edit);
            mCityAdapter.setEditMode(false);
        } else {
            mBackBtn.setVisibility(View.GONE);
            mEditBtn.setImageResource(R.drawable.btn_city_manager_edit_complete);
            mCityAdapter.setEditMode(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFavoriteCitySource.unregisterDataChangeListener(this);
        mFavoriteCitySource.unregisterDataChangeListener(mCityAdapter);
        mGlobalStateSource.unregisterSelectCityIndexListener(mCityAdapter);
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
}
