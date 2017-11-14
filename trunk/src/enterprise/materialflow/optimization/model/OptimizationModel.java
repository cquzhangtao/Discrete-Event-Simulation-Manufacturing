package enterprise.materialflow.optimization.model;

import ilog.concert.IloIntExpr;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.plan.ProductionPlanUtilities;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.Job;
import enterprise.materialflow.model.plan.order.job.JobUtilities;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;
import ui.IGanntDataset;

public class OptimizationModel implements IOptimizationModel,IGanntDataset {
	private List<IProcessActivity> activities=new ArrayList<IProcessActivity>();
	private List<IResource> resources=new ArrayList<IResource>();
	private List<IJob> jobs=new ArrayList<IJob>();
	private KeyPerformanceEnmu objective;
	private OptimizationType optimizationType;
	
	public OptimizationModel(IEnterpriseModel model){
//		for(IPlant plant:model.getPlants()){
//			resources.addAll(plant.getResources().values());
//		}
		
		for(IManufactureOrder order:ProductionPlanUtilities.generatorBOMOrders(model.getProductionPlan())){
			for(int i=0;i<order.getAmount();i++){
				IJob job=new Job(order);
				jobs.add(job);
				activities.addAll(job.getActivities().values());
			}
		}
		for(IProcessActivity act:activities){
			for(IResource res:act.getResourceRequirement().getAlternativeResources()){
				if(!resources.contains(res)){
					resources.add(res);
				}
			}
		}
	}

	public List<IProcessActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<IProcessActivity> activities) {
		this.activities = activities;
	}

	public List<IResource> getResources() {
		return resources;
	}

	public void setResources(List<IResource> resources) {
		this.resources = resources;
	}

	public List<IJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<IJob> jobs) {
		this.jobs = jobs;
	}

	@Override
	public List<IResource> getGanntResources() {
		return resources;
	}

	@Override
	public List<IProcessActivity> getGanntActivities() {
		return activities;
	}

	@Override
	public KeyPerformanceEnmu getObjective() {
		return objective;
	}

	public void setObjective(KeyPerformanceEnmu objective) {
		this.objective = objective;
	}
	
	public double getAverageCycleTime(){
		long sum=0;
		for (IJob job : jobs) {
			long startedTime = Integer.MAX_VALUE;
			long finishedTime = Integer.MIN_VALUE;
			for (IActivity act : JobUtilities.getStartActivities(job)) {
				startedTime = Math.min(startedTime, act.getStartTime());
			}
			for (IActivity act : JobUtilities.getEndActivities(job)) {
				finishedTime = Math.max(finishedTime, act.getEndTime());
			}
			sum+=finishedTime-job.getReleasedTime();
		}
		return 1.0*sum/jobs.size();
	}
	public double getMakespan(){
		double max=Double.MIN_VALUE;
		for(IActivity act:activities){
			if(max<act.getEndTime()){
				max=act.getEndTime();
			}
		}
		return max;
	}
	public double getSpan(){
		double sum=0;
		for(IActivity act1:activities){
			IProcessActivity act=(IProcessActivity) act1;
			for(IResource res:act.getResourceRequirement().getAlternativeResources()){
			sum+=act.getProcessTime().getProcessTime(res);
			}
		}
		return sum;
	}

	@Override
	public double getObjectiveValue() {
		
		if(objective==KeyPerformanceEnmu.averageCT){
			return getAverageCycleTime();
		}else{
			return getMakespan();
		}
	}

	public OptimizationType getOptimizationType() {
		return optimizationType;
	}

	public void setOptimizationType(OptimizationType optimizaitonType) {
		this.optimizationType = optimizaitonType;
	}

}
