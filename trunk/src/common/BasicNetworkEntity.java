package common;

import java.util.ArrayList;
import java.util.List;

public class BasicNetworkEntity<T> extends Entity implements INetworkEntity<T> {
	
	private List<T> predecessors;
	private List<T> successors;
	
	public BasicNetworkEntity(){
		predecessors=new ArrayList<T>();
		successors=new ArrayList<T>();
	}
	
	public BasicNetworkEntity(boolean clone){
		super(clone);
	}
	
	public List<T> getPredecessors() {
		return predecessors;
	}
	public void setPredecessors(List<T> predecessors) {
		this.predecessors = predecessors;
	}
	public List<T> getSuccessors() {
		return successors;
	}
	public void setSuccessors(List<T> successors) {
		this.successors = successors;
	}
	public void clone(INetworkEntity<T> entity) {
		
		entity.setPredecessors(predecessors);
		entity.setSuccessors(successors);
		super.clone(entity);
	}
	


}
