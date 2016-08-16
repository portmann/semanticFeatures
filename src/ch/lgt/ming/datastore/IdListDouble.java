package ch.lgt.ming.datastore;

import ch.lgt.ming.extraction.sentnence.Extractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ming Deng on 8/16/2016.
 */
public class IdListDouble implements Datastore<Integer,List<Double>> {

    private Map<Integer,List<Double>> mapStore = new HashMap<>();

    @Override
    public List<Double> getValue(Integer key) {
        return this.mapStore.get(key);
    }

    @Override
    public void putValue(Integer key, List<Double> value) {

        this.mapStore.put(key, value);
    }

    @Override
    public Map<Integer, List<Double>> getMap() {
        return this.mapStore;
    }

    @Override
    public void setMap(Map<Integer, List<Double>> map) {
        this.mapStore = map;
    }
}
