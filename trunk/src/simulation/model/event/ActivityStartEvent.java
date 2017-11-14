package simulation.model.event;

import java.util.List;

import simulation.core.event.ISimulationEvent;

public class ActivityStartEvent extends ActivityRelatedEvent{

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		return getActivity().start(currentTime);
	}

}
