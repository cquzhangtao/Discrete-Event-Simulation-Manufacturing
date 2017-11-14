package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;
import simulation.core.event.ISimulationEvent;

public class ResourceSelectingActivityEvent extends ResourceRelatedEvent{
	

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return getResource().selectActivity(currentTime);
	}
	
	public ISimulationEvent clone(){
		ResourceSelectingActivityEvent event=new ResourceSelectingActivityEvent();
		super.clone(event);
		return event;
	}

}
