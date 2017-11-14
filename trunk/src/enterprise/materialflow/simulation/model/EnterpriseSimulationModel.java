package enterprise.materialflow.simulation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Entity;
import enterprise.materialflow.control.release.BasicOrderRelease;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.plan.ProductionPlanUtilities;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.SetupableResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.processtime.IProcessTime;
import enterprise.materialflow.model.product.activity.processtime.ProcessTimeElement;
import enterprise.materialflow.model.product.activity.require.IDirectResourceRequirement;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import enterprise.materialflow.model.product.activity.require.ISkillResourceRequirement;
import simulation.model.activity.IActivity;
import ui.IGanntDataset;

public class EnterpriseSimulationModel extends Entity implements
		IEnterpriseSimulationModel,IGanntDataset {

	private Map<String, IProduct> products = new HashMap<String, IProduct>();
	private Map<String, IResource> resources = new HashMap<String, IResource>();
	private Map<String, IManufactureOrder> orders = new HashMap<String, IManufactureOrder>();	
	private Map<String, IProcessActivity> activities = new HashMap<String, IProcessActivity>();
	private Map<String, IJob> onlineJobs = new HashMap<String, IJob>();
	private Map<String, IJobRelease> jobReleases = new HashMap<String, IJobRelease>();
	private IOrderRelease orderRelease;
	private Map<String, IResourceRequirement> resourceRequirements = new HashMap<String, IResourceRequirement>();
	private Map<String, IProcessTime> processTimes = new HashMap<String, IProcessTime>();


	public EnterpriseSimulationModel(IEnterpriseModel model) {
		for (IManufactureOrder order : ProductionPlanUtilities
				.generatorBOMOrders(model.getProductionPlan())) {
			orders.put(order.getName(), order);
			jobReleases.put(order.getJobRelease().getName(),
					order.getJobRelease());
			products.put(order.getProduct().getName(), order.getProduct());
			onlineJobs.putAll(order.getOnlineJobs());
		}
		for (IPlant plant : model.getPlants()) {
			resources.putAll(plant.getResources());
		}
		setOrderRelease(new BasicOrderRelease(new ArrayList<IManufactureOrder>(orders.values())));
		for(IProduct product:products.values()){
			for(IActivity activity:product.getActivities().values()){
				if(activity instanceof IProcessActivity){
					IResourceRequirement req=((IProcessActivity) activity).getResourceRequirement();
					resourceRequirements.put(req.getName(), req);
					IProcessTime time=((IProcessActivity) activity).getProcessTime();
					processTimes.put(time.getName(), time);
				}
				
			}
		}
	}

	private EnterpriseSimulationModel() {

	}

	@Override
	public void updateActivity() {
		activities.clear();
		for (IManufactureOrder order : orders.values()) {
			for (IJob job : order.getOnlineJobs().values()) {
				activities.putAll(job.getActivities());
			}
		}
	}
	@Override
	public void updateOnlineJob() {
		onlineJobs.clear();
		for (IManufactureOrder order : orders.values()) {
			onlineJobs.putAll(order.getOnlineJobs());
		}
	}

	@Override
	public Map<String, IResource> getResources() {
		return resources;
	}

	@Override
	public void setResources(Map<String, IResource> resources) {
		this.resources = resources;
	}

	@Override
	public Map<String, IManufactureOrder> getOrders() {
		return orders;
	}

	@Override
	public void setOrders(Map<String, IManufactureOrder> orders) {
		this.orders = orders;
	}

	@Override
	public Map<String, IProcessActivity> getActivities() {
		return activities;
	}

	@Override
	public void setActivities(Map<String, IProcessActivity> activities) {
		this.activities = activities;
	}

	public IOrderRelease getOrderRelease() {
		return orderRelease;
	}

	public void setOrderRelease(IOrderRelease orderRelease) {
		this.orderRelease = orderRelease;
	}

	
	public Map<String, IJobRelease> getJobReleases() {
		return jobReleases;
	}

	public void setJobReleases(Map<String, IJobRelease> jobReleases) {
		this.jobReleases = jobReleases;
	}

	@Override
	public Map<String, IProduct>  getProducts() {
		return products;
	}

	@Override
	public void setProducts(Map<String, IProduct> products) {
		this.products=products;		
	}

	@Override
	public IEnterpriseSimulationModel clone() {
		IEnterpriseSimulationModel newModel = new EnterpriseSimulationModel();
		clone(newModel);
		EnterpriseSimulationlModelUtilities.cloneEnterpriseSimulationModel(this, newModel);
		return newModel;
	}

	@Override
	public void clone(IEnterpriseSimulationModel model) {
		super.clone(model);
		
	}

	public Map<String, IResourceRequirement> getResourceRequirements() {
		return resourceRequirements;
	}

	public void setResourceRequirements(
			Map<String, IResourceRequirement> resourceRequirements) {
		this.resourceRequirements = resourceRequirements;
	}

	public Map<String, IProcessTime> getProcessTimes() {
		return processTimes;
	}

	public void setProcessTimes(Map<String, IProcessTime> processTimes) {
		this.processTimes = processTimes;
	}

	@Override
	public Map<String, IJob> getOnlineJobs() {
		return onlineJobs;
	}
	
	public List<IProcessActivity> getFinishedActivities(){
		List<IProcessActivity> activities=new ArrayList<IProcessActivity>();
		for(IManufactureOrder order:getOrders().values()){
			for(IJob job:order.getFinishedJobs()){
				activities.addAll(job.getActivities().values());
			}			
		}
		return activities;
	}

	@Override
	public List<IResource> getGanntResources() {
		return new ArrayList<IResource>(resources.values());
	}

	@Override
	public List<IProcessActivity> getGanntActivities() {
		return getFinishedActivities();
	}
}
