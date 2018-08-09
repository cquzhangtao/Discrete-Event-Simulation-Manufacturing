package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.List;

public class Step implements IStep{
	
	private int requiredResourceNum=1;
	private long processTime;
	private IResourceGroup resourceGroup;
	public void setResourceGroup(IResourceGroup resourceGroup) {
		this.resourceGroup = resourceGroup;
	}

	private List<IStep> nextStpes=new ArrayList<IStep>();

	@Override
	public int getRequiredResourceNum() {
		return requiredResourceNum;
	}

	@Override
	public long getProcessTime() {

		return processTime;
	}

	@Override
	public BatchingConfiguration getBatchingConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasResourceJob() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IJob getResourceJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResourceGroup getRequiredResourceGroup() {
		return resourceGroup;
	}

	@Override
	public ISplittingConfig getSplittingConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IStep> getNextSteps() {
		return nextStpes;
	}

	@Override
	public void setProcessTime(long time) {
		processTime=time;
		
	}

	@Override
	public void setNextStep(IStep step) {
		nextStpes.add(step);
		
	}

}
