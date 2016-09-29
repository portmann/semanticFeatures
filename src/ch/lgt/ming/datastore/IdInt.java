package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ming Deng on 8/25/2016.
 */
public class IdInt implements Datastore<Integer,Integer> {

    private Map<Integer,Integer> mapStore = new HashMap<>();

    @Override
    public Integer getValue(Integer key) {
        return this.mapStore.get(key);
    }

    @Override
    public void putValue(Integer key, Integer value) {
        this.mapStore.put(key, value);
    }

    @Override
    public Map<Integer, Integer> getMap() {
        return this.mapStore;
    }

    @Override
    public void setMap(Map<Integer, Integer> map) {
        this.mapStore = map;
    }
}
