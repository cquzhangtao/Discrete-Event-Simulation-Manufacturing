package com.tao.fab.sim.event;

import java.util.List;

public interface IStep {

	int getRequiredResourceNum();

	long getProcessTime();

	BatchingConfiguration getBatchingConfig();

	boolean hasResourceJob();

	IJob getResourceJob();

	IResourceGroup getRequiredResourceGroup();

	ISplittingConfig getSplittingConfig();

	List<IStep> getNextSteps();
	void setNextStep(IStep step);

	void setProcessTime(long time);

	void setResourceGroup(IResourceGroup rg);

}
