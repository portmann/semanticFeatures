package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;

public class StringId implements Datastore <String, Integer>{
	
	private Map<String, Integer> mapStore = new HashMap<>();

	public StringId() {

	}

	@Override
	public Integer getValue(String key) {
		return this.mapStore.get(key);

	}

	@Override
	public void putValue(String key, Integer value) {
		this.mapStore.put(key, value);

	}

	@Override
	public Map<String, Integer> getMap() {
		return this.mapStore;
	}

	@Override
	public void setMap(Map<String, Integer> map) {
		this.mapStore = map;

	}


}
