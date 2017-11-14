package simulation.model.event;
import java.util.List;

import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;


public class ActivityEndEvent extends ActivityRelatedEvent{
	
	
	public ActivityEndEvent(){
		setType(SimulationEventType.normal);
	}

	@Override
	public List<ISimulationEvent> response(long time) {
		return getActivity().end(time);
	}

	public ISimulationEvent clone(){
		ActivityEndEvent event=new ActivityEndEvent();
		super.clone(event);
		return event;
	}

}
