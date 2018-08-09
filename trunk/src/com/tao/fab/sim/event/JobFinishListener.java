package com.tao.fab.sim.event;

import java.util.List;

import simulation.core.event.ISimulationEvent;

public interface JobFinishListener {
	//public void onJobFinish(IJob job);

	List<ISimulationEvent> onJobFinish(IJob job, long time);
}
