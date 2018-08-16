package com.tao.fab.sim.event;

import java.util.ArrayList;

public class ConnectedSteps extends ArrayList<ParallelSteps>{
	
	public ParallelSteps selectAlternativeSteps(IJob job){
		return get(0);
	}

	public boolean finished() {
		for(ParallelSteps steps:this){
			if(steps.finished()){
				return true;
			}
		}
		return false;
	}

}
