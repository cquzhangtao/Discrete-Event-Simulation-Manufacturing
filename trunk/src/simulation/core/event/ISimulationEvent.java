package simulation.core.event;
import java.io.Serializable;
import java.util.List;

import common.Clonable;


public interface ISimulationEvent extends Comparable<ISimulationEvent>,Clonable<ISimulationEvent>,Serializable{
	public long getTime();
	public void setTime(long time);
	public List<ISimulationEvent> response(long currentTime);
	public SimulationEventType getType();
	public void setType(SimulationEventType type);
	public int getPriority();
	public void setPriority(int value);

}
