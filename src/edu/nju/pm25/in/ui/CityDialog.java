package edu.nju.pm25.in.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xengine.android.utils.XStringUtil;
import edu.nju.pm25.in.R;
import edu.nju.pm25.in.data.cache.AllCitySource;
import edu.nju.pm25.in.data.cache.DataRepo;
import edu.nju.pm25.in.data.cache.FavoriteCitySource;
import edu.nju.pm25.in.data.cache.SourceName;
import edu.nju.pm25.in.data.model.CityBaseInfo;
import edu.nju.pm25.in.logic.CityMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jasontujun
 * Date: 13-6-24
 * Time: 下午8:41
 */
public class CityDialog extends Dialog {

    private ImageButton mCloseBtn;
    private EditText mCityInput;
    private GridView mCityGridView;
    private ListView mCityListView;
    private AllCityAdapter mAllCityAdapter;
    private ResultCityAdapter mResultCityAdapter;

    public CityDialog(Context context) {
        super(context);
    }

    public CityDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CityDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_search_city);
        mCloseBtn = (ImageButton) findViewById(R.id.close_btn);
        mCityInput = (EditText) findViewById(R.id.city_input);
        mCityGridView = (GridView) findViewById(R.id.city_gridview);
        mCityListView = (ListView) findViewById(R.id.city_listview);

        mAllCityAdapter = new AllCityAdapter(getContext());
        mCityGridView.setAdapter(mAllCityAdapter);
        mResultCityAdapter = new ResultCityAdapter(getContext());
        mCityListView.setAdapter(mResultCityAdapter);

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityDialog.this.dismiss();
            }
        });
        // 搜索栏设置
        mCityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = mCityInput.getText().toString();
                if (XStringUtil.isNullOrEmpty(s)) {
                    refresh(false);
                } else {
                    List<CityBaseInfo> result = CityMgr.getInstance().getFilteredCityList(s);// 任何情况下都全局搜索
                    mResultCityAdapter.refresh(result);
                    refresh(true);
                }
            }
        });
    }

    private void refresh(boolean hasInput) {
        if (hasInput) {
            mCityGridView.setVisibility(View.GONE);
            mCityListView.setVisibility(View.VISIBLE);
        } else {
            mCityGridView.setVisibility(View.VISIBLE);
            mCityListView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCityInput.requestFocus();
        refresh(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class AllCityAdapter extends BaseAdapter {

        private Context mContext;
        private AllCitySource mAllCitySource;
        private FavoriteCitySource mFavoriteCitySource;

        private AllCityAdapter(Context context) {
            mContext = context;
            mAllCitySource = (AllCitySource) DataRepo.getInstance().getSource(SourceName.ALL_CITY);
            mFavoriteCitySource = (FavoriteCitySource) DataRepo.getInstance().getSource(SourceName.FAVORITE_CITY);
        }

        @Override
        public int getCount() {
            return mAllCitySource.size();
        }

        @Override
        public Object getItem(int i) {
            return mAllCitySource.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private class ViewHolder {
            private View bgView;
            private TextView cityView;
        }
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_city_search_gridview, null);
                viewHolder = new ViewHolder();
                viewHolder.bgView = convertView.findViewById(R.id.bg);
                viewHolder.cityView = (TextView) convertView.findViewById(R.id.city_name_txt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final CityBaseInfo city = (CityBaseInfo) getItem(i);
            viewHolder.cityView.setText(city.getChineseName());
            if (mFavoriteCitySource.getIndexById(city.getChineseName()) == -1) {
                viewHolder.bgView.setBackgroundResource(android.R.color.transparent);
            } else {
                viewHolder.bgView.setBackgroundResource(R.drawable.bg_city_selected);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 添加城市
                    if (!CityMgr.getInstance().addCity(city.getChineseName()))
                        Toast.makeText(mContext, "对不起，城市已经添加了!",
                                Toast.LENGTH_SHORT).show();
                    else
                        CityDialog.this.dismiss();
                }
            });
            return convertView;
        }
    }

    private class ResultCityAdapter extends BaseAdapter {

        private Context mContext;
        private FavoriteCitySource mFavoriteCitySource;
        private List<CityBaseInfo> mResultCityList;

        private ResultCityAdapter(Context context) {
            mContext = context;
            mResultCityList = new ArrayList<CityBaseInfo>();
            mFavoriteCitySource = (FavoriteCitySource) DataRepo.getInstance().getSource(SourceName.FAVORITE_CITY);
        }

        public void refresh(List<CityBaseInfo> resultCityList) {
            mResultCityList = resultCityList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mResultCityList.size();
        }

        @Override
        public Object getItem(int i) {
            return mResultCityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private class ViewHolder {
            private View bgView;
            private TextView cityView;
        }
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_city_search_listview, null);
                viewHolder = new ViewHolder();
                viewHolder.bgView = convertView.findViewById(R.id.bg);
                viewHolder.cityView = (TextView) convertView.findViewById(R.id.city_name_txt);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final CityBaseInfo city = (CityBaseInfo) getItem(i);
            viewHolder.cityView.setText(city.getChineseName());
            if (mFavoriteCitySource.getIndexById(city.getChineseName()) == -1) {
                viewHolder.bgView.setBackgroundResource(android.R.color.transparent);
            } else {
                viewHolder.bgView.setBackgroundResource(R.drawable.bg_city_selected);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 添加城市
                    if (!CityMgr.getInstance().addCity(city.getChineseName()))
                        Toast.makeText(mContext, "对不起，城市已经添加了!",
                                Toast.LENGTH_SHORT).show();
                    else
                        CityDialog.this.dismiss();
                }
            });
            return convertView;
        }
    }
}
