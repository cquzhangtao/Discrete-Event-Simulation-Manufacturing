package com.tao.fab.sim.event;

import java.util.List;

public interface IResource {

	boolean isSeized();

	boolean canProcessJob(IJob job);

	void setPriority(double priority);

	void seize();

	void setCurrentJob(IJob job);

	boolean hasPrepareJob();

	IJob getPrepareJob();

	boolean hasCleanUpJob();

	IJob getCleanUpJob();

	void release();

	boolean hasInterruptionJob();

	List<IJob> getInterruptionJobs();

	IResourceGroup getResourceGroup();

	void setInterruptionJob(IJob ijob);

	long getNextInterruptionTime();

	void setToolGroup(IResourceGroup resourceGroup);

	boolean isBlocked();

	IJob getCurrentJob();

	void unblock();

	void block();

	ProcessType getProcessType();

	IJob getInterruptionJob();

}
