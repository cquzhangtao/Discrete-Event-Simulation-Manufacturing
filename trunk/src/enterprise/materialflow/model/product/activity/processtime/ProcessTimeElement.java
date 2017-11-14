package enterprise.materialflow.model.product.activity.processtime;


import java.io.Serializable;

import enterprise.materialflow.model.plant.resource.IResource;
import basic.volume.TimeVolume;


public class ProcessTimeElement implements Serializable{
	
	private IResource resource;
	private int amount=1;
	private TimeVolume time;
	
	public ProcessTimeElement(){
	}
	public ProcessTimeElement(IResource resource){
		this();
		this.resource=resource;
	}
	public TimeVolume getTime() {
		return time;
	}
	public void setTime(TimeVolume time) {
		this.time = time;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getAmount() {
		return amount;
	}
	public void setResource(IResource resource) {
		this.resource = resource;
	}
	public IResource getResource() {
		return resource;
	}
	public ProcessTimeElement clone(){
		ProcessTimeElement element=new ProcessTimeElement();
		element.setResource(resource);
		element.setAmount(amount);
		element.setTime(time);
		return element;
	}
}
