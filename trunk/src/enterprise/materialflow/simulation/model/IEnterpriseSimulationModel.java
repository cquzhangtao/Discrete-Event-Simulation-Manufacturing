package enterprise.materialflow.simulation.model;

import java.util.List;
import java.util.Map;

import common.IEntity;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.processtime.IProcessTime;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import simulation.model.activity.IActivity;

public interface IEnterpriseSimulationModel extends IEntity{

	public abstract void updateActivity();

	public abstract Map<String, IResource> getResources();

	public abstract void setResources(Map<String, IResource> resources);

	public abstract Map<String, IManufactureOrder> getOrders();

	public abstract void setOrders(Map<String, IManufactureOrder> orders);

	public abstract Map<String, IProcessActivity> getActivities();

	public abstract void setActivities(Map<String, IProcessActivity> activities);
	
	public IOrderRelease getOrderRelease();
	
	public void setOrderRelease(IOrderRelease orderRelease);

	public abstract IEnterpriseSimulationModel clone();

	public abstract void setJobReleases(Map<String, IJobRelease> jobReleases);
	public Map<String,IJobRelease> getJobReleases() ;

	public abstract Map<String, IProduct> getProducts();

	public abstract void setProducts(Map<String, IProduct> products);

	public abstract void clone(IEnterpriseSimulationModel model);
	
	public Map<String, IResourceRequirement> getResourceRequirements() ;

	public void setResourceRequirements(
			Map<String, IResourceRequirement> resourceRequirements) ;

	public Map<String, IProcessTime> getProcessTimes() ;

	public void setProcessTimes(Map<String, IProcessTime> processTimes) ;

	public void updateOnlineJob();

	public abstract Map<String,IJob> getOnlineJobs();
	public List<IProcessActivity> getFinishedActivities();

}