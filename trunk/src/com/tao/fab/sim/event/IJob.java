package com.tao.fab.sim.event;

import java.util.List;

public interface IJob extends ISimEntity{

	//JobType getType();
	//void setType(JobType lot);

	List<IJob> getChildren();

	List<IStep> getCurrentSteps();
	
	//IStep getCurrentStep(IResourceGroup rg);

	void addAssignedResource(IStep currentStep, IResource selRes);

	void addJobFinishListener(JobFinishListener jobFinishListener);

	//void start();
	
	IRoute getRoute();

	void oneResourceReady();

	boolean isAllResourcesReady();

	boolean canBatch(IJob job);

	 IJob newInstance() ;

	//void setBatchConfig(BatchingConfiguration bconfig);

	boolean batchReadyToGo(IResource res, long time);

	boolean goToNextStep();

	List<IResource> getAssignedResources(IStep currentStep);

	void setPriority(double priority);

	IJob getFather();

	void oneChildrenDone();

	boolean allChildrenDone();

	List<JobFinishListener> getFinishedListeners();

	IJob clone();

	//boolean isReorganizedJobInCurrentStep();

	//job is one child of a original job
	//boolean fromCompleteSplitting();

	// a new job with some children of a original job
	//boolean fromPartialSplitting();

	//a new job with some other original jobs as children
	//boolean fromCompleteCombining();

	//a new job with some children of some original jobs
	//boolean fromPartialCombining();

	//IStep getCurrentStep(IResourceGroup rg);

	void setCurrentStep(IStep currentStep);

	IStep getCurrentStep();

	IStep getPreviousStep();

	//void setReorganizedJobACurrentStep(boolean b);

	//void setPartialSplitting(boolean b);

	//void setPartialCombining(boolean partial);

	//IJob getFatherAtCurrentStep();

	//IJob getFatherBeforeReorganizedAtCurrentStep();
	
	void setRoute(IRoute route);
	boolean canNextStepAcceptMe();
	boolean canReleaseResourcesNow();
	IStep getNextStep();
	IJob getCurrentFather();
	void setReorganizeJobConfig(ReorganizeJobConfig bconfig);

	boolean isReorganizedAtCurrentStep();
	

	//void setCompleteCombining(boolean b);

	//void setCompleteSplitting(boolean b);



	

}
