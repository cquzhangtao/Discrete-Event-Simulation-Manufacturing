package com.tao.fab.sim.event;

public class Resource implements IResource{
	
	private boolean seized=false;
	private IJob currrentJob;
	private IResourceGroup resourceGroup;

	@Override
	public boolean isSeized() {
		return seized;
	}

	@Override
	public boolean canProcessJob(IJob job) {
		return true;
	}

	@Override
	public void setPriority(double priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seize() {
		seized=true;
	}

	@Override
	public void setCurrentJob(IJob job) {
		currrentJob=job;
		
	}

	@Override
	public boolean hasPrepareJob() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJob getPrepareJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCleanUpJob() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJob getCleanUpJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void release() {
		seized=true;
		
	}

	@Override
	public boolean hasInterruptionJob() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJob getInterruptionJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResourceGroup getResourceGroup() {
		return resourceGroup;
	}

	@Override
	public void setInterruptionJob(IJob ijob) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getNextInterruptionTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
