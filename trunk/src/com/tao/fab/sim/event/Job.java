package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job implements IJob{
	
	private JobType type;
	
	private List<IJob> children;
	private IJob father;
	
	private List<IStep> currentSteps;
	
	private List<JobFinishListener> finishListeners=new ArrayList<JobFinishListener>();
	
	private Map<IStep,List<IResource>> assignedResources=new HashMap<IStep,List<IResource>>();
	
	
	private List<IStep> firstSteps;
	
	
	
	

	@Override
	public JobType getType() {
		return type;
	}

	@Override
	public List<IJob> getChildren() {
		
		return children;
	}

	@Override
	public List<IStep> getCurrentSteps() {
		
		return currentSteps;
	}

	@Override
	public void addAssignedResource(IStep step, IResource selRes) {
		List<IResource> ress = assignedResources.get(step);
		if(ress==null){
			ress=new ArrayList<IResource>();
			assignedResources.put(step, ress);
		}
		ress.add(selRes);
	}

	@Override
	public void addJobFinishListener(JobFinishListener jobFinishListener) {
		finishListeners.add(jobFinishListener);
		
	}
	
	private int readyResourceCounter=0;

	@Override
	public void oneResourceReady() {
		
		readyResourceCounter++;
	}

	@Override
	public boolean isAllResourcesReady() {
		
		return readyResourceCounter==currentStep.getRequiredResourceNum();
	}

	@Override
	public boolean canBatch(IJob job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBatchConfig(BatchingConfiguration bconfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean batchReadyToGo(IResource res, long time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean goToNextStep() {
		// TODO Auto-generated method stub
		readyResourceCounter=0;
		return false;
	}

	@Override
	public List<IResource> getAssignedResources(IStep currentStep) {
		
		return assignedResources.get(currentStep);
	}

	@Override
	public void setPriority(double priority) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IJob getFather() {
		
		return father;
	}
	
	
	int childDoneCounter=0;

	@Override
	public void oneChildrenDone() {
		childDoneCounter++;
		
	}

	@Override
	public boolean allChildrenDone() {
		
		return getChildren().size()==childDoneCounter;
	}

	@Override
	public List<JobFinishListener> getFinishedListeners() {
	
		return finishListeners;
	}

	@Override
	public IJob clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
