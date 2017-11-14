package enterprise.materialflow.model.product.activity;

import java.util.List;

import enterprise.materialflow.control.routing.IResourceSelector;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.Operation;
import enterprise.materialflow.model.product.activity.processtime.IProcessTime;
import enterprise.materialflow.model.product.activity.processtime.ResourceDependentProcessTime;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public interface IProcessActivity extends IActivity{

	public void setResourceRequirement(IResourceRequirement require);

	public void setResourceSelector(IResourceSelector resourceSelector);

	public IProcessTime getProcessTime();

	public IResourceRequirement getResourceRequirement();
	
	public IProcessActivity clone();
	public IResource getAssignedResource();
	public void setAssignedResource(IResource iResource);
	public void setJob(IJob job);
	public IJob getJob();

	public void setProcessTime(IProcessTime processTime);

	public List<ISimulationEvent> selectResource(long currentTime);

	public List<ISimulationEvent> start(IResource resource, long currentTime);

	public void clone(IProcessActivity activityEx);

	public void setOperation(Operation operation);

	public void setAssignedResourceAmount(int assignedResourceAmount);
	

}
