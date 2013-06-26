package edu.nju.pm25.in.ui;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.xengine.android.data.cache.XDataChangeListener;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.FavoriteCitySource;
import edu.nju.pm25.in.data.cache.GlobalStateSource;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.model.CityDetailInfo;
import edu.nju.pm25.in.logic.CityMgr;
import edu.nju.pm25.in.util.PMUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午9:04
 */
public class CityGridViewAdapter extends BaseAdapter
        implements XDataChangeListener<CityDetailInfo>, GlobalStateSource.XCityIndexListener {

    private CityMgrActivity mContext;
    private boolean mIsEdit;
    private FavoriteCitySource mFavoriteCitySource;
    private GlobalStateSource mGlobalStateSource;

    public CityGridViewAdapter(CityMgrActivity context) {
        mContext = context;
        mFavoriteCitySource = (FavoriteCitySource) DataRepo.getInstance().getSource(SourceName.FAVORITE_CITY);
        mGlobalStateSource = (GlobalStateSource) DataRepo.getInstance().getSource(SourceName.GLOBAL_STATE);
    }

    public void setEditMode(boolean isEdit) {
        mIsEdit = isEdit;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mIsEdit)
            return mFavoriteCitySource.size();
        else
            return mFavoriteCitySource.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i < mFavoriteCitySource.size())
            return mFavoriteCitySource.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        public View bgView;
        public TextView aqiView;
        public TextView aqiLevelView;
        public TextView pm25View;
        public TextView cityView;
        public View infoView;
        public View addView;
        public Button deleteBtn;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_city_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.bgView = convertView.findViewById(R.id.bg);
            viewHolder.aqiView = (TextView) convertView.findViewById(R.id.aqi);
            viewHolder.aqiLevelView = (TextView) convertView.findViewById(R.id.aqi_level);
            viewHolder.pm25View = (TextView) convertView.findViewById(R.id.pm25);
            viewHolder.cityView = (TextView) convertView.findViewById(R.id.city_name_txt);
            viewHolder.infoView = convertView.findViewById(R.id.info_frame);
            viewHolder.addView = convertView.findViewById(R.id.add_frame);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.delete_city_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int curIndex = i;
        if (!mIsEdit && i == getCount() - 1) {
            viewHolder.deleteBtn.setVisibility(View.GONE);
            viewHolder.cityView.setVisibility(View.GONE);
            viewHolder.infoView.setVisibility(View.GONE);
            viewHolder.addView.setVisibility(View.VISIBLE);
            viewHolder.bgView.setBackgroundResource(R.drawable.btn_bg_city_grid);
            // 设置按钮监听
            viewHolder.bgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CityDialog(mContext, R.style.dialog).show();
                }
            });
        } else {
            viewHolder.cityView.setVisibility(View.VISIBLE);
            viewHolder.infoView.setVisibility(View.VISIBLE);
            viewHolder.addView.setVisibility(View.GONE);

            final CityDetailInfo cityDetailInfo = (CityDetailInfo) getItem(i);
            viewHolder.cityView.setText(cityDetailInfo.getCity());
            viewHolder.aqiView.setText("AQI: " + cityDetailInfo.getAqi());
            viewHolder.pm25View.setText("PM2.5: " + cityDetailInfo.getPm25());
            String quality = cityDetailInfo.getQuality();
            viewHolder.aqiLevelView.setText(quality);
            viewHolder.aqiLevelView.setTextColor(PMUtil.getColorByQuality(mContext, quality));

            // 判断是否是当前选中的
            if (mGlobalStateSource.getSelectCityIndex() == i)
                viewHolder.bgView.setBackgroundResource(R.drawable.btn_city_grid_bg_select);
            else
                viewHolder.bgView.setBackgroundResource(R.drawable.btn_bg_city_grid);

            // 判断是否是编辑模式
            if (mIsEdit) {
                viewHolder.deleteBtn.setVisibility(View.VISIBLE);
                viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 删除城市
                        CityMgr.getInstance().deleteCity(cityDetailInfo.getCity());
                    }
                });
                viewHolder.bgView.setOnClickListener(null);
            } else {
                viewHolder.deleteBtn.setVisibility(View.GONE);
                viewHolder.bgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 切换到当前城市
                        mGlobalStateSource.setSelectCityIndex(curIndex);
                        mContext.finish();
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public void onChange(int i) {
        postNotifyDataChange();
    }


    @Override
    public void onChange() {
        postNotifyDataChange();
    }

    @Override
    public void onAdd(CityDetailInfo cityDetailInfo) {
        postNotifyDataChange();
    }

    @Override
    public void onAddAll(List<CityDetailInfo> cityDetailInfos) {
        postNotifyDataChange();
    }

    @Override
    public void onDelete(CityDetailInfo cityDetailInfo) {
        postNotifyDataChange();
    }

    @Override
    public void onDeleteAll(List<CityDetailInfo> cityDetailInfos) {
        postNotifyDataChange();
    }

    private void postNotifyDataChange() {
        changeHandler.sendEmptyMessage(0);
    }

    private Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                notifyDataSetChanged();
            }
        }
    };
}
