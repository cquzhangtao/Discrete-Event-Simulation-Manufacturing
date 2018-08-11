package com.tao.fab.sim.event;

import java.util.List;

import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public abstract class AbstractEvent {

	
	private long time;
	
	public abstract void response(SimulationEventList eventList,long currentTime);
	


	public long getTime() {
		
		return time;
	}


	public void setTime(long time) {
		this.time=time;
		
	}




	public SimulationEventType getType() {

		return SimulationEventType.normal;
	}

	

}
