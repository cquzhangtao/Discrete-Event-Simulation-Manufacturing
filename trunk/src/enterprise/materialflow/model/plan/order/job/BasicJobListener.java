package enterprise.materialflow.model.plan.order.job;

import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.InfiniteManufactureOrder;
import enterprise.materialflow.simulation.result.SimulationResultNew;

public class BasicJobListener implements JobListener{
	
	private IManufactureOrder order;
	
	public BasicJobListener(IManufactureOrder order){
		this.order=order;
	}

	@Override
	public void onCompleted(IJob job, long time) {
		job.setCompletedTime(time);
		job.setState(JobState.finished);
		order.getOnlineJobs().remove(job.getName());
		SimulationResultNew.addProductData(order.getProduct(), time, time-job.getReleasedTime());	
		if(!(order instanceof InfiniteManufactureOrder)){
			order.getFinishedJobs().add(job);
		}
		//System.out.println(time+job.getName()+" finished");
	}

	@Override
	public void onReleased(IJob job, long time) {
		job.setReleasedTime(time);
		job.setState(JobState.started);
		order.getOnlineJobs().put(job.getName(),job);
		SimulationResultNew.addProductData(order.getProduct(), time);
		
		//System.out.println(time+job.getName()+" released");
	}

}
