package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job extends SimEntity implements IJob{
	

	
	private List<IJob> children;
	private IJob father;
	
	private List<IStep> currentSteps;
	private IStep currentStep;
	private IStep previousStep;
	private List<JobFinishListener> finishListeners=new ArrayList<JobFinishListener>();
	
	private Map<IStep,List<IResource>> assignedResources=new HashMap<IStep,List<IResource>>();
	
	
	private IRoute route;
	
	
	public Job(){
		//this.route=route;
		//currentSteps=route.getFirstSteps();
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
		
		return readyResourceCounter==getCurrentStep().getRequiredResourceNum();
	}

	@Override
	public boolean canBatch(IJob job) {
		return false;
	}

	
	@Override
	public boolean batchReadyToGo(IResource res, long time) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean goToNextStep() {
		readyResourceCounter=0;
		previousStep=currentStep;
		if(currentSteps==null){
			currentSteps=route.getFirstSteps();
		}else{
			currentSteps=currentStep.getNextSteps();
		}
		currentStep=null;
		return !currentSteps.isEmpty();
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
		IJob njob=new Job();
		njob.setRoute(route);
		return njob;
	}
	
	public void clone(IJob job){
		job.setRoute(route);
		super.clone(job);
	}

	@Override
	public IJob newInstance() {
		// TODO Auto-generated method stub
		return new Job();
	}
	
	

	@Override
	public void setCurrentStep(IStep currentStep) {
		this.currentStep=currentStep;
		
	}

	@Override
	public IStep getCurrentStep() {
		
		return currentStep;
	}

	@Override
	public IStep getPreviousStep() {
		return previousStep;
	}

	



	

	@Override
	public IRoute getRoute() {
		return route;
	}


	@Override
	public void setRoute(IRoute route) {
		this.route=route;
		//currentSteps=route.getFirstSteps();
		
	}




	@Override
	public boolean canNextStepAcceptMe() {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean canReleaseResourcesNow() {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public IStep getNextStep() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public IJob getCurrentFather() {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void setReorganizeJobConfig(ReorganizeJobConfig bconfig) {
		// TODO Auto-generated method stub
		
	}


	

}
