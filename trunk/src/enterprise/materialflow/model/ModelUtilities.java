package enterprise.materialflow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.control.release.CONWIPJobRelease;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.control.routing.MinQueueLenResourceSelector;
import enterprise.materialflow.control.routing.STTDResourceSelector;
import enterprise.materialflow.control.sequencing.FIFOActivitySelector;
import enterprise.materialflow.control.sequencing.STTDActivitySelector;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.EnterpriseSimulation;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.core.ISimulation;
import simulation.model.activity.IActivity;

public class ModelUtilities {
	
	public static void setSTTDActivitySelector(IEnterpriseSimulation sim,KeyPerformanceEnmu kpi){
		STTDActivitySelector selector=new STTDActivitySelector();
		selector.setSimulation(sim);
		selector.setBaseActivitySelector(new FIFOActivitySelector());
		selector.setBaseResourceSelector(new MinQueueLenResourceSelector());
		selector.setObjective(kpi);
		for(IResource resource:sim.getEnterpriseSimulationModel().getResources().values()){			
			resource.setActivitySelector(selector);
		}
	}
	public static void setSTTDResourceSelector(IEnterpriseSimulation sim,KeyPerformanceEnmu kpi){
		STTDResourceSelector selector=new STTDResourceSelector();
		selector.setSimulation(sim);
		selector.setBaseActivitySelector(new FIFOActivitySelector());
		selector.setBaseResourceSelector(new MinQueueLenResourceSelector());
		selector.setObjective(kpi);
		IEnterpriseSimulationModel newModel=sim.getEnterpriseSimulationModel();
		for (IActivity activity : newModel.getActivities().values()) {
			if (activity instanceof IProcessActivity) {
				((IProcessActivity) activity)
						.setResourceSelector(selector);
			}
		}
		for(IProduct product:newModel.getProducts().values()){
			for(IActivity activity:product.getActivities().values()){
				((IProcessActivity) activity)
				.setResourceSelector(selector);
			}
		}
	}
	public static void setCONWIPJobReleases(IEnterpriseSimulation sim){
		Random rnd = new Random(1);
		Map<IProduct,CONWIPJobRelease> releases=new HashMap<IProduct,CONWIPJobRelease>();
		for(IManufactureOrder order:sim.getEnterpriseSimulationModel().getOrders().values()){
			CONWIPJobRelease release=releases.get(order.getProduct());
			if(release==null){
			release=new CONWIPJobRelease(sim, order.getProduct());
			release.setTargetWip(10+rnd.nextInt(10));
			releases.put(order.getProduct(), release);
			}
			order.setJobRelease(release);
		}
	}
//	public static List<IActivity> getActivities(IEnterpriseModel model){
//		return null;
//	}
//	
//	public static IActivity getActivity(IEnterpriseModel model,String id){
//		return null;
//	}
//	
//	public static List<IResource> getResources(IEnterpriseModel model){
//		return null;
//	}
//	
//	public static IResource getResource(IEnterpriseModel model,String id){
//		return null;
//	}
//	
//	public static List<IManufactureOrder> getOrders(IEnterpriseModel model){
//		return null;
//		
//	}
//	
//	public static IManufactureOrder getOrder(IEnterpriseModel model,String id){
//		return null;
//		
//	}
//	
//	public static IJobRelease getJobRelease(IEnterpriseModel model,String id){
//		return null;
//	}
//	
//	public static IOrderRelease getOrderRelease(IEnterpriseModel model,String id){
//		return null;
//	}
//	
//	
//	public static List<IResource> getAllResources(IEnterpriseModel model){
//		List<IResource> resources=new ArrayList<IResource>();
//		for(IPlant plant:model.getPlants()){
//			resources.addAll(plant.getResources());
//		}
//		return resources;
//	}
	
//	public static List<IActivity> getAllActivities(IEnterpriseModel model){
//		List<IActivity> activities=new ArrayList<IActivity>();
//		for(IManufactureOrder order:model.getProductionPlan().getBomOrders()){
//			for(IJob job:order.getJobs()){
//				activities.addAll(job.getActivities());
//			}
//			
//		}
//		return activities;
//	}
//	public static List<IProduct> getAllProducts(IEnterpriseModel model){
//		List<IProduct> products=new ArrayList<IProduct>();
//		for(IManufactureOrder order:model.getProductionPlan().getBomOrders()){
//			if(products.contains(order.getProduct())){
//				continue;
//			}
//			products.add(order.getProduct());
//			
//		}
//		return products;
//	}

}
