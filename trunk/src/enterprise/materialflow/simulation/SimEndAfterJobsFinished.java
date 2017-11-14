package enterprise.materialflow.simulation;

import java.util.List;

import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.JobState;
import simulation.core.others.ISimulationTerminateCondition;

public class SimEndAfterJobsFinished implements ISimulationTerminateCondition{
	
	private List<IJob>jobs;
	
	public SimEndAfterJobsFinished(List<IJob>jobs){
		this.jobs=jobs;
	}

	@Override
	public boolean isSatisfied(long time) {
		for(IJob job:jobs){
			if(!(job.getState()==JobState.finished)){
				//System.out.println(job.getName()+"is not finished");
				return false;
			}
			//System.out.println(job.getName()+"is finished");
		}
		return true;
	}

	@Override
	public ISimulationTerminateCondition clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
