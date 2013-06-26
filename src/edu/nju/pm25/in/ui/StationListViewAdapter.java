package edu.nju.pm25.in.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xengine.android.data.cache.XDataChangeListener;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.cache.StationBaseSource;
import edu.nju.pm25.in.data.model.StationBaseInfo;
import edu.nju.pm25.in.util.PMUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-23
 * Time: 下午4:19
 */
public class StationListViewAdapter extends BaseAdapter implements XDataChangeListener<StationBaseInfo> {

    private Context mContext;
    private StationBaseSource mStationBaseSource;
    private String mCity;
    private List<StationBaseInfo> mStationList;

    public StationListViewAdapter(Context context, String city) {
        mContext = context;
        mStationBaseSource = (StationBaseSource) DataRepo.
                getInstance().getSource(SourceName.STATION_BASE);
        mStationBaseSource.registerDataChangeListener(this);// 对数据源注册监听
        refresh(city);
    }

    @Override
    protected void finalize() throws java.lang.Throwable {
        super.finalize();

        if (mStationBaseSource != null)
            mStationBaseSource.unregisterDataChangeListener(this);// 取消对数据源的监听
    }

    public void refresh(String city) {
        mCity = city;
        mStationList = mStationBaseSource.getByCityName(mCity);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStationList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        public View bgView;
        public TextView stationNameView;
        public ImageView aqiIcon;
        public TextView aqiView;
        public TextView aqiLevelView;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_station_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.bgView = convertView.findViewById(R.id.bg);
            viewHolder.stationNameView = (TextView) convertView.findViewById(R.id.station_name);
            viewHolder.aqiIcon = (ImageView) convertView.findViewById(R.id.aqi_icon);
            viewHolder.aqiView = (TextView) convertView.findViewById(R.id.aqi);
            viewHolder.aqiLevelView = (TextView) convertView.findViewById(R.id.aqi_level);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final StationBaseInfo stationBaseInfo = (StationBaseInfo) getItem(i);
        viewHolder.stationNameView.setText(stationBaseInfo.getPosition_name());
        viewHolder.aqiView.setText("AQI: " + stationBaseInfo.getAqi());
        String quality = stationBaseInfo.getQuality();
        viewHolder.aqiIcon.setImageResource(PMUtil.getIconByQuality(quality));
        viewHolder.aqiLevelView.setText(quality);
        viewHolder.aqiLevelView.setTextColor(PMUtil.getColorByQuality(mContext, quality));

        viewHolder.bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StationInfoActivity.class);
                intent.putExtra("cityName", mCity);
                intent.putExtra("stationName", stationBaseInfo.getPosition_name());
                intent.putExtra("stationCode", stationBaseInfo.getStation_code());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
    @Override
    public void onChange() {
        postNotifyDataChange();
    }

    @Override
    public void onAdd(StationBaseInfo stationBaseInfos) {
        postNotifyDataChange();
    }

    @Override
    public void onAddAll(List<StationBaseInfo> stationBaseInfos) {
        postNotifyDataChange();
    }

    @Override
    public void onDelete(StationBaseInfo stationBaseInfo) {
        postNotifyDataChange();
    }

    @Override
    public void onDeleteAll(List<StationBaseInfo> stationBaseInfo) {
        postNotifyDataChange();
    }


    private void postNotifyDataChange() {
        changeHandler.sendEmptyMessage(0);
    }

    private Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                refresh(mCity);
            }
        }
    };
}
