package com.tao.fab.sim.event;

import simulation.core.ISimulation;
import simulation.core.event.ISimulationEvent;

public class SimulationEventList {
	
	public void add(AbstractEvent event,long nextime){
		event.setTime(nextime+getSimulation().getCurrentTime());
	}

	private ISimulation getSimulation() {
		// TODO Auto-generated method stub
		return null;
	}

}
