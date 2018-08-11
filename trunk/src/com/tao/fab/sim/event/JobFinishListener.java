package com.tao.fab.sim.event;

import java.util.List;

import simulation.core.event.ISimulationEvent;

public interface JobFinishListener {
	//public void onJobFinish(IJob job);

	void onJobFinish(SimulationEventList eventList,IJob job, long time);
}
