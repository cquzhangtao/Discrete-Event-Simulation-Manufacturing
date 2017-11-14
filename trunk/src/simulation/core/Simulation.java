package simulation.core;
import java.util.ArrayList;
import java.util.List;

import common.Entity;
import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;
import simulation.core.others.ISimulationTerminateCondition;


public class Simulation extends Entity implements ISimulation{
	
	private List<ISimulationEvent> eventList=new ArrayList<ISimulationEvent>();	
	private List<SimulationListener> listeners=new ArrayList<SimulationListener>();
	private List<ISimulationTerminateCondition> endConditions=new ArrayList<ISimulationTerminateCondition>();
	private long currentTime;	
	private boolean running;
	
	public void run(){
		running=true;
		while(running&&!eventList.isEmpty()&&!AreAllPeriodicalEvents()&&!meetEndCondition()){
			ISimulationEvent event=getEarliestEvent();
			if(event.getTime()<currentTime){
				System.out.println("error....");
				System.exit(0);
			}
			eventList.remove(event);
			currentTime=event.getTime();
			eventList.addAll(event.response(currentTime));
		}
	}
	
	private boolean meetEndCondition(){
		if(endConditions.isEmpty()){
			return false;
		}
		for(ISimulationTerminateCondition condition:endConditions){
			if(condition.isSatisfied(currentTime)){
				return true;
			}
		}
		return false;
	}
	
	public void addEvents(List<ISimulationEvent> events){
		eventList.addAll(events);
	}
	
	private ISimulationEvent getEarliestEvent(){
		List<ISimulationEvent> earliestEvents=new ArrayList<ISimulationEvent>();
		long min=Long.MAX_VALUE;
		//ISimulationEvent earliestEvent=null;
		for(ISimulationEvent event:eventList){
			if(min>event.getTime()){
				min=event.getTime();
				earliestEvents.clear();
				earliestEvents.add(event);
			}else if(min==event.getTime()){
				earliestEvents.add(event);
			}
		}
		List<ISimulationEvent> highPrioEvents=new ArrayList<ISimulationEvent>();
		int high=Integer.MAX_VALUE;
		for(ISimulationEvent event:earliestEvents){
			if(high>event.getPriority()){
				high=event.getPriority();
				highPrioEvents.clear();
				highPrioEvents.add(event);
			}else{
				highPrioEvents.add(event);
			}
			
		}
		return highPrioEvents.get(0);
	}
	private boolean AreAllPeriodicalEvents(){
		for(ISimulationEvent event:eventList){
			if(event.getType()!=SimulationEventType.periodical){
				return false;
			}
		}
		return true;
	}
		
	public List<ISimulationEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<ISimulationEvent> eventList) {
		this.eventList = eventList;
	}
	
	public void addEvent(ISimulationEvent event){
		eventList.add(event);
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}
	
	public void addEndCondition(ISimulationTerminateCondition con){
		endConditions.add(con);		
	}
	
	public void clone(ISimulation simulation){
		simulation.setCurrentTime(currentTime);
		List<ISimulationEvent> eventList=new ArrayList<ISimulationEvent>();
		for(ISimulationEvent event:this.eventList){
			eventList.add(event.clone());
		}
		simulation.setEventList(eventList);
		List<ISimulationTerminateCondition> endConditions=new ArrayList<ISimulationTerminateCondition>();
		for(ISimulationTerminateCondition con:this.endConditions){
			endConditions.add(con.clone());
		}
		simulation.setEndConditions(endConditions);
		super.clone(simulation);
	}

	public List<ISimulationTerminateCondition> getEndConditions() {
		return endConditions;
	}

	public void setEndConditions(List<ISimulationTerminateCondition> endConditions) {
		this.endConditions = endConditions;
	}

	public List<SimulationListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<SimulationListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void stop() {
		running=false;
		
	}


}
