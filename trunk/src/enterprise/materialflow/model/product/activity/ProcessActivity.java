package enterprise.materialflow.model.product.activity;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.routing.IResourceSelector;
import enterprise.materialflow.control.routing.MinQueueLenResourceSelector;
import enterprise.materialflow.control.routing.STTDResourceSelector;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.Operation;
import enterprise.materialflow.model.product.activity.condition.PrecedenceConstraint;
import enterprise.materialflow.model.product.activity.processtime.IProcessTime;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import enterprise.materialflow.simulation.event.ActivitySelectingResourceEvent;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.Activity;
import simulation.model.activity.IActivity;

public class ProcessActivity extends Activity implements IProcessActivity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2749471140918324400L;
	private Operation operation;
	private IJob job;
	private IResourceRequirement resourceRequirement;
	private IProcessTime processTime;
	private IResource assignedResource;
	private int assignedResourceAmount;
	private IResourceSelector resourceSelector;

	public ProcessActivity() {
		super();
		// addStartCondition(new ResourceConstraint());
		addStartCondition(new PrecedenceConstraint());
		resourceSelector = new MinQueueLenResourceSelector();
		// processTime=new ProcessTime();
	}

	public ProcessActivity(boolean clone) {
		super(clone);
	}

	public IProcessTime getProcessTime() {
		return processTime;
	}

	public void setProcessTime(IProcessTime processTime) {
		this.processTime = processTime;
	}

	public IResource getAssignedResource() {
		return assignedResource;
	}

	public void setAssignedResource(IResource assignedResource) {
		this.assignedResource = assignedResource;
	}

	public int getAssignedResourceAmount() {
		return assignedResourceAmount;
	}

	public void setAssignedResourceAmount(int assignedResourceNum) {
		this.assignedResourceAmount = assignedResourceNum;
	}

	@Override
	public List<ISimulationEvent> end(long time) {
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		events.addAll(super.end(time));
		// System.out.println(time+","+this.getName()+" is finished by resource "+assignedResource.getName()+"+");
		events.addAll(assignedResource.free(this, time));
		for (IActivity act : getSuccessors()) {
			if (act.isStartable()) {
				ProcessActivity pact = (ProcessActivity) act;
				if (pact.getResourceSelector() instanceof STTDResourceSelector) {
					ActivitySelectingResourceEvent event = new ActivitySelectingResourceEvent();
					event.setActivity(pact);
					event.setTime(time);
					events.add(event);
				} else {					
					IResource res = pact.getResourceSelector().selectResource(
							pact, pact.getAlternativeResources());
					pact.setAssignedResource(res);
					events.addAll(res.queue(pact, time));

				}

			}
		}
		return events;
	}

	@Override
	public List<ISimulationEvent> start(long time) {
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		events.addAll(super.start(time));
		// System.out.println(time+","+this.getName()+" is started by resource "+assignedResource.getName()+"-");
		return events;
	}

	@Override
	public long getDuration() {
		return processTime.getProcessTime(assignedResource,
				assignedResourceAmount);
	}

	public IResourceRequirement getResourceRequirement() {
		return resourceRequirement;
	}

	public void setResourceRequirement(IResourceRequirement resourceRequirement) {
		this.resourceRequirement = resourceRequirement;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public List<IResource> getAlternativeResources() {
		return resourceRequirement.getAlternativeResources();
	}

	public IResourceSelector getResourceSelector() {
		return resourceSelector;
	}

	public void setResourceSelector(IResourceSelector resSelector) {
		this.resourceSelector = resSelector;
	}

	public IProcessActivity clone() {
		IProcessActivity activity = new ProcessActivity(true);
		clone(activity);		
		return activity;
	}
	
	public void clone(IProcessActivity activity){
		activity.setOperation(operation);
		activity.setJob(job);
		activity.setResourceRequirement(resourceRequirement);
		activity.setProcessTime(processTime);
		activity.setAssignedResource(assignedResource);
		activity.setAssignedResourceAmount(assignedResourceAmount);
		activity.setResourceSelector(resourceSelector);
		super.clone(activity);
	}

	public boolean canBeProcessedOn(String resource) {
		for (IResource res : this.getAlternativeResources()) {
			if (res.getName().equals(resource)) {
				return true;
			}
		}
		return false;
	}

	public IJob getJob() {
		return job;
	}

	public void setJob(IJob job) {
		this.job = job;
	}

	@Override
	public List<ISimulationEvent> selectResource(long currentTime) {
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		IResource res = resourceSelector.selectResource(this,
				this.getAlternativeResources());
		this.setAssignedResource(res);
		events.addAll(res.queue(this, currentTime));
		return events;
	}
	public List<ISimulationEvent> start(IResource res,long currentTime) {
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		this.setAssignedResource(res);
		events.addAll(res.queue(this, currentTime));
		return events;
	}
}
