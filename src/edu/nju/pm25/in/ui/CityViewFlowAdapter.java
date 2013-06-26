package edu.nju.pm25.in.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.xengine.android.data.cache.XDataChangeListener;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.FavoriteCitySource;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.model.CityDetailInfo;
import edu.nju.pm25.in.util.PMUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-19
 * Time: 下午2:09
 */
public class CityViewFlowAdapter extends BaseAdapter implements XDataChangeListener<CityDetailInfo> {

    private Context mContext;
    private FavoriteCitySource mFavoriteCitySource;

    public CityViewFlowAdapter(Context context) {
        mContext = context;
        mFavoriteCitySource = (FavoriteCitySource) DataRepo.getInstance().getSource(SourceName.FAVORITE_CITY);
    }

    @Override
    public int getCount() {
        return mFavoriteCitySource.size();
    }

    @Override
    public Object getItem(int i) {
        return mFavoriteCitySource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        public TextView aqiView;
        public TextView aqiLevelView;
        public TextView pm25View;
        public TextView influenceView;
        public TextView adviceView;
        public ListView stationListView;
        public TextView stationListTipView;
        public TextView updateView;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_city_info, null);
            viewHolder = new ViewHolder();
            viewHolder.aqiView = (TextView) convertView.findViewById(R.id.aqi);
            viewHolder.aqiLevelView = (TextView) convertView.findViewById(R.id.aqi_level);
            viewHolder.pm25View = (TextView) convertView.findViewById(R.id.pm25);
            viewHolder.influenceView = (TextView) convertView.findViewById(R.id.influence);
            viewHolder.adviceView = (TextView) convertView.findViewById(R.id.advice);
            viewHolder.stationListView = (ListView) convertView.findViewById(R.id.station_list);
            viewHolder.stationListTipView = (TextView) convertView.findViewById(R.id.update_tip);
            viewHolder.updateView = (TextView) convertView.findViewById(R.id.update_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CityDetailInfo cityDetailInfo = (CityDetailInfo) getItem(i);
        viewHolder.aqiView.setText(cityDetailInfo.getAqi());
        viewHolder.pm25View.setText("PM2.5： " + cityDetailInfo.getPm25());
        viewHolder.updateView.setText(cityDetailInfo.getTime_point() + "更新");
        String quality = cityDetailInfo.getQuality();
        viewHolder.aqiLevelView.setText(quality);
        viewHolder.aqiLevelView.setTextColor(PMUtil.getColorByQuality(mContext, quality));
        viewHolder.influenceView.setText("健康影响：" + PMUtil.getHealthInfluenceByQuality(quality));
        viewHolder.adviceView.setText("温馨提醒：" + PMUtil.getAdviceByQuality(quality));

        // stationList
        StationListViewAdapter stationAdapter = (StationListViewAdapter) viewHolder.stationListView.getAdapter();
        if (stationAdapter == null) {
            stationAdapter = new StationListViewAdapter(mContext, cityDetailInfo.getCity());
            viewHolder.stationListView.setAdapter(stationAdapter);
        } else {
            stationAdapter.refresh(cityDetailInfo.getCity());
        }
        if (stationAdapter.getCount() == 0) {
            viewHolder.stationListView.setVisibility(View.GONE);
            viewHolder.stationListTipView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.stationListView.setVisibility(View.VISIBLE);
            viewHolder.stationListTipView.setVisibility(View.GONE);
        }

        return convertView;
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
