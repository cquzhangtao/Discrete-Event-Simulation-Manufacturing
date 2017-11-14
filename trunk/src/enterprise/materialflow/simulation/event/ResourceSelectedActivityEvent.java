package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;
import simulation.core.event.BasicSimulationEvent;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public class ResourceSelectedActivityEvent extends BasicSimulationEvent{
	
	private IActivity activity;
	private IResource resource;

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return resource.start(activity,currentTime);
	}

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

	public IResource getResource() {
		return resource;
	}

	public void setResource(IResource resource) {
		this.resource = resource;
	}

	
}
