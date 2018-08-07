package com.tao.fab.sim.event;

public interface IProduct {

	long getTimeToNextRelease();

	IProductJob getJob();
	
	

}
