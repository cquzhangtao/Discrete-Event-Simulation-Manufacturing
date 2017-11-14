package simulation.model.event;

import simulation.core.event.BasicSimulationEvent;
import simulation.model.activity.IActivity;

public abstract class ActivityRelatedEvent extends BasicSimulationEvent{
	private IActivity activity;

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}
	
	public void clone(ActivityRelatedEvent act){
		act.setActivity(activity);
		super.clone(act);
	}

}
