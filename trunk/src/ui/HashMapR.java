package ui;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("serial")
public class HashMapR<K, V> extends HashMap<K,V>{
	private Map<V,K> reverse=new HashMap<V,K>();
	
	@Override
	public V put(K k, V v){
		super.put(k,v);
		reverse.put(v, k);
		return v;
		
	}

	public Collection<K> valuesR(){
		return reverse.values();
	}
	
	public Set<V> keySetR(){
		return reverse.keySet();
	}
	
	public K getR( V v){
		return reverse.get(v);
	}

}
