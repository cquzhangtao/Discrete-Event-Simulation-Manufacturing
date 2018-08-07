package com.tao.fab.sim.event;



public interface IStep {

	int getRequiredResourceNum();

	long getProcessTime();

	BatchingConfiguration getBatchingConfig();

	boolean hasResourceJob();

	IJob getResourceJob();

	IResourceGroup getRequiredResourceGroup();

	ISplittingConfig getSplittingConfig();

}
