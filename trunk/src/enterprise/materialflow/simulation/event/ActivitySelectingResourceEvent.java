package enterprise.materialflow.simulation.event;

import java.util.List;

import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.core.event.ISimulationEvent;
import simulation.model.event.ActivityRelatedEvent;

public class ActivitySelectingResourceEvent extends ActivityRelatedEvent{
	
	@Override
	public List<ISimulationEvent> response(long currentTime) {
		if(getActivity() instanceof IProcessActivity){
			return ((IProcessActivity) getActivity()).selectResource(currentTime);
		}
		return null;
		
		
	}
	
	public ISimulationEvent clone(){
		ActivitySelectingResourceEvent event=new ActivitySelectingResourceEvent();
		super.clone(event);
		return event;
	}

}
