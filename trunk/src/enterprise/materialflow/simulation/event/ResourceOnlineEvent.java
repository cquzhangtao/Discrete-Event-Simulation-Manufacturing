package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IInterruptableResource;
import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public class ResourceOnlineEvent extends ResourceRelatedEvent{
	
	private ResourceOnlineEvent(){
		
	}
	public ResourceOnlineEvent(IInterruptableResource res){
		setResource(res);
		setType(SimulationEventType.periodical);
	}

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return ((IInterruptableResource)getResource()).online(currentTime);
	}
	
	public ResourceOnlineEvent clone(){
		ResourceOnlineEvent event=new ResourceOnlineEvent();
		super.clone(event);
		return event;
	}

}
