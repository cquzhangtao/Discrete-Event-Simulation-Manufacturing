package com.tao.fab.sim.event;

import java.util.List;

public interface IProduct extends ISimEntity{

	long getTimeToNextRelease();

	IProductJob getJob();

	void setProductJob(IProductJob job);

	void addJobToReleaseQueue(IProductJob pjob);

	List<IProduct> getAllProducts();

	List<IJob> getReleaseQueue();

	boolean isReleaseQueueFull();

	boolean isReleaseQueueEmpty();

	void stopRelease();

	boolean isReleaseStopped();

	void continueReleaseJob();

	
	
	

}
