package util;

/**
 * 键值对包装类
 * @author luminocean
 *
 * @param <K>
 * @param <V>
 */
public class Pair<K,V> {
	public K key;
	public V value;
	
	public Pair(K key, V value){
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
