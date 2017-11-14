package enterprise.materialflow.optimization.model;

import enterprise.materialflow.model.plant.resource.IResource;
import simulation.model.activity.IActivity;

public class Mode {
	private IActivity owner;
	private int time;
	private IResource resource;
	
	public Mode(IActivity owner,IResource resource,int l){
		this.owner=owner;
		this.resource=resource;
		this.time=l;
	}
	
	public IActivity getOwner() {
		return owner;
	}
	public void setOwner(IActivity owner) {
		this.owner = owner;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public IResource getResource() {
		return resource;
	}
	public void setResource(IResource resource) {
		this.resource = resource;
	}

}
