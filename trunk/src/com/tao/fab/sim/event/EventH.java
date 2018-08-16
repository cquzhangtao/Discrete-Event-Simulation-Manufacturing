package com.tao.fab.sim.event;

public class EventH {

	
	public void startJob(IJob job){
		ConnectedSteps altSteps = job.getRoute().getFirstAlternativeSteps();
		ParallelSteps steps = altSteps.selectAlternativeSteps(job);
		for(IStep step:steps){
			enterStep(step);
		}
	}
	
	public void enterStep(IStep step){
		
	}
	
	private boolean canJobMoveToNextSteps(IJob job,IStep step){
		return false;
	}
	
	public void onStepFinish(IJob job,IStep step){
		if(canJobMoveToNextSteps(job,step)){
			moveToNextSteps(job,step);
		}else if(canJobPutInRearQueue(job,step)){
			putJobInRearQueue(job,step);
			
		}else{
			blockResources(job,step);
		}
	}
	
	private void blockResources(IJob job, IStep step) {
		// TODO Auto-generated method stub
		
	}

	private boolean canJobPutInRearQueue(IJob job, IStep step) {
		// TODO Auto-generated method stub
		return false;
	}

	private void onJobEnterRearQueue(IJob job, IStep step) {
		// TODO Auto-generated method stub
		
	}

	private void putJobInRearQueue(IJob job, IStep step) {
		
		SeizedResources ress=step.getSeizedResources();
		
		for(IResourceGroup rg:ress.keySet()){
			rg.addJobToRearQueue(job);
		}
		
	}

	public void moveToNextSteps(IJob job,IStep step){
		ParallelSteps steps = step.getSucessors().selectAlternativeSteps(job);
		for(IStep istep:steps){
			if(istep.getPredecessors().finished()){
				enterStep(istep);
			}
		}
	}
	
	
}
