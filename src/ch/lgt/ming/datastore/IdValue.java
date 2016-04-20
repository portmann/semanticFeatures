package ch.lgt.ming.datastore;

import java.util.HashMap;
import java.util.Map;

public class IdValue implements Datastore<Integer, Double>{
	
	private Map<Integer, Double> mapStore = new HashMap<>();

	public IdValue() {

	}

	@Override
	public Double getValue(Integer key) {
		return this.mapStore.get(key);
	}

	@Override
	public void putValue(Integer key, Double value) {
		this.mapStore.put(key, value);

	}

	@Override
	public Map<Integer, Double> getMap() {
		return this.mapStore;
	}

	@Override
	public void setMap(Map<Integer, Double> map) {
		this.mapStore = map;

	}

}
