package simulation.model;

import java.util.ArrayList;
import java.util.List;

import simulation.core.event.ISimulationEvent;
import simulation.model.activity.ActivityState;
import simulation.model.activity.IActivity;
import simulation.model.event.ActivityStartEvent;
import common.Entity;

public class SimulationModel extends Entity implements ISimulationModel{
	List<IActivity> activities;

	public List<IActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<IActivity> activities) {
		this.activities = activities;
	}

	@Override
	public List<ISimulationEvent> getInitialEvents() {
		List<ISimulationEvent>  events=new ArrayList<ISimulationEvent>();
		for(IActivity act:activities){
			if(act.getPredecessors().isEmpty()){
				ActivityStartEvent event=new ActivityStartEvent();
				event.setActivity(act);
				event.setTime(0);
				events.add(event);
			}
		}
		return events;
	}

	@Override
	public void reset() {
		for(IActivity act:activities){
			act.setState(ActivityState.waiting);
		}
		
	}
	

}
