package enterprise.materialflow.simulation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.SetupableResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.processtime.IProcessTime;
import enterprise.materialflow.model.product.activity.processtime.ProcessTimeElement;
import enterprise.materialflow.model.product.activity.require.IDirectResourceRequirement;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import enterprise.materialflow.model.product.activity.require.ISkillResourceRequirement;
import enterprise.materialflow.simulation.event.JobReleaseEvent;
import enterprise.materialflow.simulation.event.OrderReleaseEvent;
import enterprise.materialflow.simulation.event.ResourceRelatedEvent;
import simulation.core.ISimulation;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;
import simulation.model.event.ActivityRelatedEvent;

public class EnterpriseSimulationlModelUtilities {
	public static void cloneEnterpriseSimulationModel(
			IEnterpriseSimulationModel original,
			IEnterpriseSimulationModel newModel) {
		Map<String, IResource> resources = new HashMap<String, IResource>();

		for (IResource resource : original.getResources().values()) {
			IResource newRes = resource.clone();
			resources.put(newRes.getName(), newRes);
		}
		newModel.setResources(resources);

		Map<String, IResourceRequirement> resourceRequirements = new HashMap<String, IResourceRequirement>();
		for (IResourceRequirement req : original.getResourceRequirements()
				.values()) {
			IResourceRequirement newreq = req.clone();
			resourceRequirements.put(newreq.getName(), newreq);
			if (newreq instanceof ISkillResourceRequirement) {
				((ISkillResourceRequirement) newreq)
						.setAllResources(new ArrayList<IResource>(resources
								.values()));
			} else if (newreq instanceof IDirectResourceRequirement) {
				List<IResource> altRess = new ArrayList<IResource>();
				for (IResource res : newreq.getAlternativeResources()) {
					altRess.add(resources.get(res.getName()));
				}
				((IDirectResourceRequirement) newreq).setAlternativeResources(altRess);
			}
		}
		newModel.setResourceRequirements(resourceRequirements);
		
		Map<String, IProcessTime> processTimes = new HashMap<String, IProcessTime>();
		for(IProcessTime time:original.getProcessTimes().values()){
			IProcessTime newtime=time.clone();
			for(ProcessTimeElement element:newtime.getProcessTimeMap()){
				element.setResource(resources.get(element.getResource().getName()));
			}
			processTimes.put(newtime.getName(), newtime);
		}
		newModel.setProcessTimes(processTimes);

		Map<String, IJobRelease> jobReleases = new HashMap<String, IJobRelease>();
		Map<String, IManufactureOrder> orders = new HashMap<String, IManufactureOrder>();
		Map<String, IProduct> products = new HashMap<String, IProduct>();
		for (IManufactureOrder order : original.getOrders().values()) {
			IManufactureOrder newOrder = order.clone();
			orders.put(newOrder.getName(), newOrder);
			jobReleases.put(newOrder.getJobRelease().getName(),
					newOrder.getJobRelease());
			if (products.containsKey(order.getProduct().getName())) {
				newOrder.setProduct(products.get(order.getProduct().getName()));
			} else {
				IProduct newProduct = order.getProduct().clone();
				products.put(newProduct.getName(), newProduct);
				newOrder.setProduct(newProduct);
			}
		}
		
		for (IProduct product : products.values()) {
			for (IActivity act : product.getActivities().values()) {
				if (act instanceof IProcessActivity) {
					IProcessTime oldtime = ((IProcessActivity) act).getProcessTime();
					IProcessTime newtime = processTimes.get(oldtime.getName());
					((IProcessActivity) act).setProcessTime(newtime);
					
					IResourceRequirement oldreq=((IProcessActivity) act).getResourceRequirement();
					IResourceRequirement newreq=resourceRequirements.get(oldreq.getName());
					((IProcessActivity) act).setResourceRequirement(newreq);					
				}
			}
		}
		for (IManufactureOrder order : orders.values()) {
			if (order.getParent() != null) {
				order.setParent(orders.get(order.getParent().getName()));
			}
		}
		newModel.setOrders(orders);
		newModel.setJobReleases(jobReleases);
		newModel.setProducts(products);
		newModel.setOrderRelease(original.getOrderRelease().clone());
		List<IManufactureOrder> remainOrders = new ArrayList<IManufactureOrder>();
		for (IManufactureOrder order : original.getOrderRelease().getOrders()) {
			remainOrders.add(orders.get(order.getName()));
		}
		newModel.getOrderRelease().setOrders(remainOrders);
		newModel.updateActivity();
		for (IActivity activity : newModel.getActivities().values()) {
			if (activity instanceof IProcessActivity) {
				if (((IProcessActivity) activity).getAssignedResource() != null) {
					((IProcessActivity) activity).setAssignedResource(newModel
							.getResources().get(
									((IProcessActivity) activity)
											.getAssignedResource().getName()));

				}
				IProcessTime oldtime = ((IProcessActivity) activity).getProcessTime();
				IProcessTime newtime = processTimes.get(oldtime.getName());
				((IProcessActivity) activity).setProcessTime(newtime);
				
				IResourceRequirement oldreq=((IProcessActivity) activity).getResourceRequirement();
				IResourceRequirement newreq=resourceRequirements.get(oldreq.getName());
				((IProcessActivity) activity).setResourceRequirement(newreq);

			}
		}
		for (IResource resource : resources.values()) {
			List<IActivity> queue = new ArrayList<IActivity>();
			for (IActivity activity : resource.getQueue()) {
				queue.add(newModel.getActivities().get(activity.getName()));
			}
			resource.setQueue(queue);
			if (resource instanceof SetupableResource) {
				// TODO update pre activty
				IActivity wact = ((SetupableResource) resource)
						.getWaitingSetupActivity();
				if (wact != null) {
					((SetupableResource) resource)
							.setWaitingSetupActivity(newModel.getActivities()
									.get(wact.getName()));
				}
			}
		}
	}

	private static void updateResourceReqirement(
			IEnterpriseSimulationModel model, IActivity activity) {
		if (((IProcessActivity) activity).getResourceRequirement() instanceof ISkillResourceRequirement) {
			((ISkillResourceRequirement) ((IProcessActivity) activity)
					.getResourceRequirement())
					.setAllResources(new ArrayList<IResource>(model
							.getResources().values()));
		} else if (((IProcessActivity) activity).getResourceRequirement() instanceof IDirectResourceRequirement) {
			List<IResource> altRess = new ArrayList<IResource>();
			for (IResource res : ((IProcessActivity) activity)
					.getResourceRequirement().getAlternativeResources()) {
				altRess.add(model.getResources().get(res.getName()));
			}
			((IDirectResourceRequirement) ((IProcessActivity) activity)
					.getResourceRequirement()).setAlternativeResources(altRess);
		} else {
			System.out.println("Error");
		}
	}

	public static void connectModelAndSimulation(
			IEnterpriseSimulationModel newModel, ISimulation subSimulation) {
		List<ISimulationEvent> events = subSimulation.getEventList();
		for (ISimulationEvent event : events) {
			if (event instanceof ActivityRelatedEvent) {
				IActivity act = newModel.getActivities().get(
						((ActivityRelatedEvent) event).getActivity().getName());
				((ActivityRelatedEvent) event).setActivity(act);
			} else if (event instanceof ResourceRelatedEvent) {
				IResource res = newModel.getResources().get(
						((ResourceRelatedEvent) event).getResource().getName());
				((ResourceRelatedEvent) event).setResource(res);
			} else if (event instanceof JobReleaseEvent) {
				IJobRelease release = newModel.getJobReleases().get(
						((JobReleaseEvent) event).getJobRelease().getName());
				((JobReleaseEvent) event).setJobRelease(release);
			} else if (event instanceof OrderReleaseEvent) {

				((OrderReleaseEvent) event).setOrderRelease(newModel
						.getOrderRelease());
			}
		}

	}
	
	public static List<IJob> updateJobs(IEnterpriseSimulationModel model,List<IJob> oldJobs){
		List<IJob> newjobs=new ArrayList<IJob>();
		for(IJob job:oldJobs){
			newjobs.add(model.getOnlineJobs().get(job.getName()));
		}
		return newjobs;
	}

}
