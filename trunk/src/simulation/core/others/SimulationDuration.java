package simulation.core.others;

import basic.volume.TimeVolume;

public class SimulationDuration implements ISimulationTerminateCondition{
	
	private TimeVolume simulationTime;
	
	public SimulationDuration(TimeVolume time){
		simulationTime=time;
	}

	@Override
	public boolean isSatisfied(long time) {
		return time>=simulationTime.getMilliSeconds();
	}
	
	public ISimulationTerminateCondition clone(){
		return new SimulationDuration(simulationTime);
	}

}
