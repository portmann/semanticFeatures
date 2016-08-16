package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ming Deng on 8/15/2016.
 */
public class IdSetString implements Datastore<Integer,Set<String>> {

    private Map<Integer, Set<String>> mapStore = new HashMap<>();


    @Override
    public Set<String> getValue(Integer key) {
        return mapStore.get(key);
    }

    @Override
    public void putValue(Integer key, Set<String> value) {
        mapStore.put(key, value);

    }

    @Override
    public Map<Integer, Set<String>> getMap() {
        return this.mapStore;
    }

    @Override
    public void setMap(Map<Integer, Set<String>> map) {
        this.mapStore = map;

    }
}
