package com.tao.fab.sim.event;

import java.util.List;

public interface IResourceGroup {

	JobType getJobType();

	//void jobEnterQueue(IJob job);

	//void jobEnterQueue(List<IJob> subJobs);

	List<IResource> getResources();

	boolean hasIdleResource();

	List<IJob> getFrontQueue();
	
	List<IJob> getFrontQueueWithOrganizedJobs();

	List<IResource> getIdleResources();

	JobType getJobTypeInQueue();

	void setResourceNumber(int i);

	void setJobTypeInFrontQueue(JobType type);

	void setJobType(JobType lot);

	IJob getPrepareJob();
	IJob getCleanUpJob();

	void setPrepareJob(IJob prepaerJob);

}
