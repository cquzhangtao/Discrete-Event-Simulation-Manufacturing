package ui;

import enterprise.materialflow.model.plant.resource.IResource;
import simulation.model.activity.IActivity;




public interface AllocationChangeListener{
	public void allocationChanged(IResource resourceNew,IActivity activity,int newpos);
}
