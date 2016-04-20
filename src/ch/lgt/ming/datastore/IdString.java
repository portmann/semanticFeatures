package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;

public class IdString implements Datastore<Integer, String> {

	private Map<Integer, String> mapStore = new HashMap<>();

	public IdString() {

	}

	@Override
	public String getValue(Integer key) {
		return this.mapStore.get(key);
	}

	@Override
	public void putValue(Integer key, String value) {
		this.mapStore.put(key, value);

	}

	@Override
	public Map<Integer, String> getMap() {
		return this.mapStore;
	}

	@Override
	public void setMap(Map<Integer, String> map) {
		this.mapStore = map;

	}

}
