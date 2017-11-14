package simulation.model;

import java.util.List;

import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;
import common.IEntity;

public interface ISimulationModel extends IEntity{
	public List<IActivity> getActivities();
	public void setActivities(List<IActivity> activities);
	public List<ISimulationEvent> getInitialEvents();
	public void reset();
}
