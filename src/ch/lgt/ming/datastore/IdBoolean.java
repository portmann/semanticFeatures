package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;

public class IdBoolean implements Datastore<Integer, Boolean> {
	
	private Map<Integer, Boolean> mapStore = new HashMap<>();

	public IdBoolean() {

	}

	@Override
	public Boolean getValue(Integer key) {

		return this.mapStore.get(key);
	}

	@Override
	public void putValue(Integer key, Boolean value) {

		this.mapStore.put(key, value);
	}

	@Override
	public Map<Integer, Boolean> getMap() {

		return this.mapStore;
	}

	@Override
	public void setMap(Map<Integer, Boolean> map) {

		this.mapStore = map;
	}

}
