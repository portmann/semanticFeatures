package ch.lgt.ming.datastore;

import java.util.Map;

public interface Datastore<K, V> {

	public V getValue(K key);
	public void putValue(K key, V value);
	public Map<K, V> getMap();
	public void setMap(Map<K, V> map);

}
