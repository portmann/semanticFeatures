package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ming Deng on 7/6/2016.
 */
public class IdListString implements Datastore<Integer, List<String >> {

    private Map<Integer, List<String>> mapStore = new HashMap<>();

    @Override
    public List<String> getValue(Integer key){

        return this.mapStore.get(key);
    }

    @Override
    public void putValue(Integer key, List<String> value){

        this.mapStore.put(key, value);
    }

    @Override
    public Map<Integer, List<String>> getMap(){

        return this.mapStore;

    }

    @Override
    public void setMap(Map<Integer, List<String>> mapStore){

        this.mapStore = mapStore;
    }

    public void addValue(Integer key, String value){

        List<String> updateList = this.mapStore.get(key);
        updateList.add(value);
        this.mapStore.put(key, updateList);
    }


}
