package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.plant.resource.ISetupableResource;
import simulation.core.event.ISimulationEvent;

public class ResourceSetupEndEvent extends ResourceRelatedEvent{
	
	private ResourceSetupEndEvent(){
		
	}
	
	public ResourceSetupEndEvent(ISetupableResource res){
		setResource(res);
	}

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return ((ISetupableResource)getResource()).endSetup(currentTime);
	}
	
	public ResourceSetupEndEvent clone(){
		ResourceSetupEndEvent event=new ResourceSetupEndEvent();
		super.clone(event);
		return event;
	}

}
