package com.tao.fab.sim.event;
import java.util.ArrayList;

import com.tao.fab.sim.event.IStep;

public class ParallelSteps extends ArrayList<IStep>{

	public boolean finished() {
		for(IStep step:this){
			if(!step.finished()){
				return false;
			}
		}
		return true;
	}

}
