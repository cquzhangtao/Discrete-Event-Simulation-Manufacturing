package enterprise.materialflow.model.product.activity;

import java.util.List;

import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.Job;
import enterprise.materialflow.model.plan.order.job.JobListener;
import enterprise.materialflow.model.plan.order.job.JobState;
import enterprise.materialflow.model.plan.order.job.JobUtilities;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import simulation.model.activity.ActivityListener;
import simulation.model.activity.IActivity;

public class BasicActivityListener implements ActivityListener{
	
	private List<IProcessActivity> unfinishedLastActivities;
	private IJob job;
	
	public BasicActivityListener(){
		
	}
	
	public BasicActivityListener(IJob job){
		this.job=job;
		unfinishedLastActivities=JobUtilities.getEndActivities(job);
	}

	@Override
	public void onEnd(IActivity activity, long time) {
		if (!activity.getSuccessors().isEmpty()) {
			return;
		}
		unfinishedLastActivities.remove(activity);
		if(unfinishedLastActivities.isEmpty()){
			for(JobListener lis:job.getListeners()){
				lis.onCompleted(job, time);
			}
		}
		
	}

	@Override
	public void onStart(IActivity activity, long time) {
		
	}

	public List<IProcessActivity> getUnfinishedLastActivities() {
		return unfinishedLastActivities;
	}

	public void setUnfinishedLastActivities(List<IProcessActivity> unfinishedLastActivities) {
		this.unfinishedLastActivities = unfinishedLastActivities;
	}

	public IJob getJob() {
		return job;
	}

	public void setJob(IJob job) {
		this.job = job;
	}
	
	public BasicActivityListener clone(){
		return new BasicActivityListener();
	}

}
