package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IInterruptableResource;
import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public class ResourceOfflineEvent extends ResourceRelatedEvent{
	
	private ResourceOfflineEvent(){
		
	}
	
	public ResourceOfflineEvent(IInterruptableResource res){
		setResource(res);
		setType(SimulationEventType.periodical);
	}

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return ((IInterruptableResource)getResource()).offline(currentTime);
	}

	public ResourceOfflineEvent clone(){
		ResourceOfflineEvent event=new ResourceOfflineEvent();
		super.clone(event);
		return event;
	}

}
