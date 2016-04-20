package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdListId implements Datastore<Integer, List<Integer>> {
	
	private Map<Integer, List<Integer>> mapStore = new HashMap<>();

	@Override
	public List<Integer> getValue(Integer key) {
		return this.mapStore.get(key);
	}

	@Override
	public void putValue(Integer key, List<Integer> value) {
		this.mapStore.put(key, value);
		
	}

	@Override
	public Map<Integer, List<Integer>> getMap() {
		return this.mapStore;
	}

	@Override
	public void setMap(Map<Integer, List<Integer>> map) {
		this.mapStore = map;
	}
	
	public void addValue(Integer key, Integer value)
	{
		List<Integer> updateList = this.mapStore.get(key);
		updateList.add(value);
		this.mapStore.put(key, updateList);
	}

}
