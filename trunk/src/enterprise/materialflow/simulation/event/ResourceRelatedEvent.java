package enterprise.materialflow.simulation.event;

import enterprise.materialflow.model.plant.resource.IResource;
import simulation.core.event.BasicSimulationEvent;
import simulation.core.event.ISimulationEvent;
import simulation.core.others.ISimulationTerminateCondition;

public abstract class ResourceRelatedEvent extends BasicSimulationEvent{
	private IResource resource;
	
	public IResource getResource() {
		return resource;
	}

	public void setResource(IResource resource) {
		this.resource = resource;
	}
	
	public void clone(ResourceRelatedEvent event){
		event.setResource(resource);
		super.clone(event);
	}
}
