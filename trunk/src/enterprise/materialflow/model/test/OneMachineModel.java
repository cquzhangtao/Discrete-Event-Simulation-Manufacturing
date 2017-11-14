package enterprise.materialflow.model.test;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import enterprise.materialflow.control.release.BasicJobRelease;
import enterprise.materialflow.control.routing.MinQueueLenResourceSelector;
import enterprise.materialflow.control.sequencing.FIFOActivitySelector;
import enterprise.materialflow.model.EnterpriseModel;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plan.IProductionPlan;
import enterprise.materialflow.model.plan.ProductionPlan;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.InfiniteManufactureOrder;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.Plant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.InterruptableResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.Product;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.ProcessActivity;
import enterprise.materialflow.model.product.activity.processtime.ResourceDependentProcessTime;
import enterprise.materialflow.model.product.activity.require.IResourceRequirement;
import enterprise.materialflow.model.product.activity.require.ISkillResourceRequirement;
import enterprise.materialflow.model.product.activity.require.SkillResourceRequirement;
import simulation.model.activity.IActivity;
import basic.distribution.TimePoint;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class OneMachineModel {

	public static IEnterpriseModel getModel(){
		List<IPlant> plants = new ArrayList<IPlant>();
		IPlant plant = new Plant();
		plants.add(plant);
		
		List<Skill> skills = new ArrayList<Skill>();
		Skill skill=new Skill();
		skills.add(skill);
		
		Map<String,IResource> resources=new HashMap<String,IResource>();
		IResource resource=new InterruptableResource();
		resource.setSkills(skills);
		resource.setActivitySelector(new FIFOActivitySelector());
		resources.put(resource.getName(), resource);
		plant.setResources(resources);
		
		List<IProduct> products = new ArrayList<IProduct>();		
		IProduct product = new Product();
		IProcessActivity activity=new ProcessActivity();
		ISkillResourceRequirement req=new SkillResourceRequirement();
		req.setAllResources(new ArrayList<IResource>(resources.values()));
		req.setNeededSkill(skill);
		activity.setResourceRequirement(req);
		ResourceDependentProcessTime time=new ResourceDependentProcessTime();
		time.addProcessTime(resource, new TimeVolume(2,TimeUnitEnum.Day));
		activity.setProcessTime(time);
		activity.setResourceSelector(new MinQueueLenResourceSelector());
		Map<String,IProcessActivity>activities=new HashMap<String,IProcessActivity>();
		activities.put(activity.getName(), activity);
		product.setActivities(activities);		
		products.add(product);
		
		List<IManufactureOrder> orders=new ArrayList<IManufactureOrder>();
		IManufactureOrder order=new InfiniteManufactureOrder(product);
		BasicJobRelease jobRelease = new BasicJobRelease(order);
		jobRelease.setInterval(new TimeVolume(1,TimeUnitEnum.Day));
		order.setJobRelease(jobRelease);
		order.setReleaseTime(new TimePoint(0d));
		orders.add(order);
		IProductionPlan plan = new ProductionPlan(orders);
		EnterpriseModel enterprise = new EnterpriseModel(plants,products,plan);
		return enterprise;
	}

}
