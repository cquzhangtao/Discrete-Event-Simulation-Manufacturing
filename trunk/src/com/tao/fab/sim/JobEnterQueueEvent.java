package com.tao.fab.sim;

import java.util.List;

import com.tao.fab.model.job.IGeneralJob;
import com.tao.fab.model.resource.IResourceGroup;

import simulation.core.event.BasicSimulationEvent;
import simulation.core.event.ISimulationEvent;

public class JobEnterQueueEvent extends BasicSimulationEvent{

	
	
	public JobEnterQueueEvent (IGeneralJob job,IResourceGroup<?> rg){
		
	}
	
	@Override
	public List<ISimulationEvent> response(long currentTime) {
		
		return null;
	}

}
