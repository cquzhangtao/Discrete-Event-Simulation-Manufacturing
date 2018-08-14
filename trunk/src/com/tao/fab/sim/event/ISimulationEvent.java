package com.tao.fab.sim.event;
import java.io.Serializable;
import java.util.List;

import common.Clonable;
import simulation.core.event.SimulationEventType;


public interface ISimulationEvent extends Comparable<ISimulationEvent>,Clonable<ISimulationEvent>,Serializable{
	public long getTime();
	public void setTime(long time);
	public void response(SimulationEventList list,long currentTime);
	public SimulationEventType getType();
	public void setType(SimulationEventType type);
	public int getPriority();
	public void setPriority(int value);

}
