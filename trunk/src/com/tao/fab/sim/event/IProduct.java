package com.tao.fab.sim.event;

public interface IProduct extends ISimEntity{

	long getTimeToNextRelease();

	IProductJob getJob();

	void setProductJob(IProductJob job);

	
	
	

}
