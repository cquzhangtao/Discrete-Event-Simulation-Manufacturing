package com.tao.fab.sim.event;

public class Product extends SimEntity implements IProduct{
	
	private IProductJob job;

	@Override
	public long getTimeToNextRelease() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public IProductJob getJob() {

		return job;
	}

	@Override
	public void setProductJob(IProductJob job) {
		
		this.job=job;
		
	}

}
