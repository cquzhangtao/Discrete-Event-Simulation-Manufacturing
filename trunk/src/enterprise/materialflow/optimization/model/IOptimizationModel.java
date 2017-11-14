package enterprise.materialflow.optimization.model;

import java.util.List;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;

public interface IOptimizationModel {

	public List<IProcessActivity> getActivities();

	public List<IResource> getResources();
	
	public KeyPerformanceEnmu getObjective();
	public void setObjective(KeyPerformanceEnmu objective);

	public List<IJob> getJobs();

	public double getSpan();

	public double getObjectiveValue();
	
	public OptimizationType getOptimizationType();

}
