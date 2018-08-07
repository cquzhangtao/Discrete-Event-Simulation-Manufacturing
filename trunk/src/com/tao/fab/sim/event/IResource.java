package com.tao.fab.sim.event;

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

	IJob getInterruptionJob();

	IResourceGroup getResourceGroup();

	void setInterruptionJob(IJob ijob);

	long getNextInterruptionTime();

}
