package common;

import java.io.Serializable;


public interface IEntity extends Serializable{

	public abstract String getUUID();

	public abstract String getName();

	public abstract String getDescription();

	public abstract void setGUId(String id);

	public abstract void setName(String name);

	public abstract void setDescription(String description);

	public abstract String getId();

	public abstract void setId(String id);
	public abstract String toString();
	public void addProperty(Object key,Object value);
	public Object getProperty(Object key);




}