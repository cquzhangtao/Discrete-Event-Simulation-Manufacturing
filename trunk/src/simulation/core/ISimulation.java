package simulation.core;

import java.util.List;

import common.IEntity;
import simulation.core.event.ISimulationEvent;
import simulation.core.others.ISimulationTerminateCondition;

public interface ISimulation extends IEntity{
	public void run();
	public void stop();
	public void setEventList(List<ISimulationEvent> eventList);
	public List<ISimulationEvent> getEventList();
	public void addEvent(ISimulationEvent event);
	public void setEndConditions(List<ISimulationTerminateCondition> conditions);
	public List<ISimulationTerminateCondition> getEndConditions();
	public void addEndCondition(ISimulationTerminateCondition condition);
	public void setCurrentTime(long currentTime);	
	public long getCurrentTime();
	public void addEvents(List<ISimulationEvent> events);

	
}
