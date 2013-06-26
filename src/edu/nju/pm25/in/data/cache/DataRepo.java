package edu.nju.pm25.in.data.cache;


import com.xengine.android.data.cache.XDataRepository;
import com.xengine.android.data.cache.XDataSource;

import java.util.HashMap;

/**
 * 数据仓库
 */
public class DataRepo implements XDataRepository {

    private static DataRepo instance;

    public synchronized static DataRepo getInstance() {
        if(instance == null) {
            instance = new DataRepo();
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    private HashMap<String, XDataSource> map;
    private DataRepo(){
        map = new HashMap<String, XDataSource>();
    }

    @Override
    public void registerDataSource(XDataSource source) {
        map.put(source.getSourceName(), source);
    }

    @Override
    public void unregisterDataSource(XDataSource source) {
        map.remove(source.getSourceName());
    }

    @Override
    public XDataSource getSource(String sourceName) {
        return map.get(sourceName);
    }
}
