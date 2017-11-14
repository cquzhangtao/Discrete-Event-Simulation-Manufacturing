package common;


import java.util.HashMap;
import java.util.Map;

public class Entity implements IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String guid;
	private String id;
	private String name;
	private String description;
	private Map<Object,Object> properties=new HashMap<Object,Object>();
	
	public Entity(){
		//guid = UUID.randomUUID().toString();
		description=" Write something here...";
		
	}
	public Entity(boolean clone){
		//do nothing here
		
	}
	
	@Override
	public String toString(){
		return name;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#getGUId()
	 */
//	@Override
//	public Object clone(){
//		Entity entity=new Entity();
//		clone(entity);
//		return entity;
//	}
	public void clone(IEntity entity){

		entity.setId(id);
		entity.setName(name);
		entity.setDescription(description);		

	}
	
	@Override
	public String getUUID() {
		return guid;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#setGUId(java.lang.String)
	 */
	@Override
	public void setGUId(String id) {
		this.guid = id;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see basic.IEntity#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public void addProperty(Object key,Object value){
		properties.put(key, value);
	}
	public Object getProperty(Object key){
		return properties.get(key);
	}
}
