package com.tao.fab.sim.event;

import java.util.List;

public interface IStep {
	
	ResourceRequirement getResourceRequirement();
	
	ConnectedSteps getPredecessors();
	ConnectedSteps getSucessors();
	
	int getRequiredResourceNum();

	long getProcessTime();


	boolean hasResourceJob();

	IJob getResourceJob();

	IResourceGroup getRequiredResourceGroup();

	

//	List<IStep> getNextSteps();
//	void setNextStep(IStep step);

	void setProcessTime(long time);

	void setResourceGroup(IResourceGroup rg);

	double getScrapRatio();

	double getSampleRatio();

	double getUnitScrapRatio();

	double getReworkingRatio();

	IStep getReworkingStep();

	ReorganizeJobConfig getReorganizeConfig();

	boolean finished();

	SeizedResources getSeizedResources();

}
