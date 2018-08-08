package com.tao.fab.sim.event;

import java.util.List;

public interface IRoute {

	List<IStep> getFirstSteps();

	void setFirstStep(IStep step);

}
