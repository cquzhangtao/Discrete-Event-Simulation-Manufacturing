package com.tao.fab.sim.event;

import java.util.List;

import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public abstract class AbstractEvent implements ISimulationEvent{

	
	private long time;
	@Override
	public int compareTo(ISimulationEvent arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ISimulationEvent clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTime() {
		
		return time;
	}

	@Override
	public void setTime(long time) {
		this.time=time;
		
	}



	@Override
	public SimulationEventType getType() {

		return SimulationEventType.normal;
	}

	@Override
	public void setType(SimulationEventType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPriority() {

		return 0;
	}

	@Override
	public void setPriority(int value) {
		// TODO Auto-generated method stub
		
	}

}
