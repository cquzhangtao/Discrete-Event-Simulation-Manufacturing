package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.List;

public class Route implements IRoute{
	
	private List<IStep> firstSteps=new ArrayList<IStep>();

	@Override
	public List<IStep> getFirstSteps() {
		
		return firstSteps;
	}

	@Override
	public void setFirstStep(IStep step) {
		firstSteps.add(step);
		
	}

}
