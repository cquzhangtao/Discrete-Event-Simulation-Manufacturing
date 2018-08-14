package com.tao.fab.sim.event;

import java.util.List;



public class SimulationEventList {
	
	
	private List<ISimulationEvent> eventList;
	private Simulation simulation;
	public SimulationEventList(Simulation sim,List<ISimulationEvent> eventList) {
		this.eventList=eventList;
		simulation=sim;
	}

	public void add(AbstractEvent event,long nextime){
		event.setTime(nextime+simulation.getCurrentTime());
		eventList.add(event);
	}

	

}
