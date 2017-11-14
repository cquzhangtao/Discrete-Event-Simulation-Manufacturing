package enterprise.materialflow.model.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import enterprise.materialflow.control.release.BasicJobRelease;
import enterprise.materialflow.control.release.BasicOrderRelease;
import enterprise.materialflow.control.routing.MinQueueLenResourceSelector;
import enterprise.materialflow.model.EnterpriseModel;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plan.IProductionPlan;
import enterprise.materialflow.model.plan.ProductionPlan;
import enterprise.materialflow.model.plan.ProductionPlanUtilities;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.InfiniteManufactureOrder;
import enterprise.materialflow.model.plan.order.ManufactureOrder;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.Plant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.InterruptableResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.Product;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.ProcessActivity;
import enterprise.materialflow.model.product.activity.processtime.ProcessingTimeLogic;
import enterprise.materialflow.model.product.activity.processtime.ResourceDependentProcessTime;
import enterprise.materialflow.model.product.activity.require.SkillResourceRequirement;
import basic.distribution.Distribution;
import basic.distribution.DistributionParameterEnum;
import basic.distribution.TimePoint;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class TestModel {

	private static boolean infinite = false;
	
	public static IEnterpriseModel finiteModel(){
		infinite=false;
		return generateEnterprise();
	}
	
	public static IEnterpriseModel infiniteModel(){
		infinite=true;
		return generateEnterprise();
	}

	private static IEnterpriseModel generateEnterprise() {
		Random rnd = new Random(0);
		List<IPlant> plants = new ArrayList<IPlant>();
		for (int i = 0; i < 2; i++) {
			IPlant plant = generatePlant(rnd.nextInt());
			plants.add(plant);
		}
		List<IProduct> products = new ArrayList<IProduct>();
		for (int i = 0; i < 60; i++) {
			IProduct product = generateProduct(plants, rnd.nextInt());
			products.add(product);
		}
//		for (IProduct product : products) {
//			product.addDependenceProduct(
//					products.get(rnd.nextInt(products.size())), 1);
//		}
		IProductionPlan plan = generateProductionPlan(products, rnd.nextInt());
		// plan.setOrderRelease(new
		// BasicOrderRelease(ProductionPlanUtilities.generatorBOMOrders(plan)));
		EnterpriseModel enterprise = new EnterpriseModel(plants, products, plan);
		return enterprise;
	}

	private static IPlant generatePlant(int seed) {

		Random rnd = new Random(seed);
		int skillNum = 20;
		List<Skill> skills = new ArrayList<Skill>();
		for (int i = 0; i < skillNum; i++) {
			Skill skill = new Skill();
			skills.add(skill);
		}

		int resNum = 10;
		List<Skill> skillsHave = new ArrayList<Skill>();
		Map<String, IResource> resources = new HashMap<String, IResource>();
		for (int i = 0; i < resNum; i++) {
			InterruptableResource resource = new InterruptableResource();
			resources.put(resource.getName(), resource);
			TimeVolume offlineTime = new TimeVolume(20, TimeUnitEnum.Minute);
			resource.setOfflineTime(offlineTime);
			TimeVolume timeToNextOffline = new TimeVolume(200,
					TimeUnitEnum.Minute);
			resource.setTimeToNextOffline(timeToNextOffline);
			for (int j = 0; j < rnd.nextInt(10) + 10; j++) {
				Skill skill = skills.get(rnd.nextInt(skillNum));
				resource.addSkill(skill);
				skillsHave.add(skill);
			}
		}

		IPlant model = new Plant();
		model.setResources(resources);
		model.setSkills(skillsHave);
		return model;
	}

	private static IProductionPlan generateProductionPlan(
			List<IProduct> products, int seed) {
		Random rnd = new Random(seed);

		List<IManufactureOrder> orders = new ArrayList<IManufactureOrder>();
		for (int i = 0; i < 1; i++) {
			if (infinite) {
				IManufactureOrder order = new InfiniteManufactureOrder(
						products.get(rnd.nextInt(products.size())));
				TimePoint time = new TimePoint(0);
				order.setReleaseTime(time);
				order.getJobRelease().setInterval(
						new TimeVolume(2.8, TimeUnitEnum.Hour));
				orders.add(order);
			} else {

				IManufactureOrder order = new ManufactureOrder(products.get(rnd
						.nextInt(products.size())));
				order.setAmount(1);
				TimePoint time = new TimePoint(0d);
				order.setReleaseTime(time);
				BasicJobRelease jobRelease = new BasicJobRelease(order);
				jobRelease.setInterval(new TimeVolume(0, TimeUnitEnum.Hour));
				order.setJobRelease(jobRelease);
				orders.add(order);
			}

		}
		IProductionPlan plan = new ProductionPlan(orders);
		return plan;
	}

	private static IProduct generateProduct(List<IPlant> plants, int seed) {
		Random rnd = new Random(seed);
		IProduct product = new Product();
		IPlant plant = plants.get(rnd.nextInt(plants.size()));
		product.setPlant(plant);
		Map<String, IProcessActivity> activities = new HashMap<String, IProcessActivity>();
		for (int i = 0; i < 12; i++) {
			IProcessActivity activity = new ProcessActivity();
			activities.put(activity.getName(), activity);
			SkillResourceRequirement require = new SkillResourceRequirement();
			require.setAllResources(new ArrayList<IResource>(plant
					.getResources().values()));
			require.setNeededSkill(plant.getSkills().get(
					rnd.nextInt(plant.getSkills().size())));
			activity.setResourceRequirement(require);
			activity.setResourceSelector(new MinQueueLenResourceSelector());
		}

		List<IProcessActivity> acts = new ArrayList<IProcessActivity>(
				activities.values());
		for (IProcessActivity activity : activities.values()) {
			if (rnd.nextDouble() > 0.2) {
				for (int j = 0; j < rnd.nextInt(2) + 2; j++) {
					activity.addPredecessor(acts.get(rnd.nextInt(acts.size())));
				}
			}
			ResourceDependentProcessTime processTime = new ResourceDependentProcessTime();
			for (IResource resource : plant.getResources().values()) {
				if (!activity.getResourceRequirement()
						.getAlternativeResources().contains(resource)) {
					continue;
				}
				TimeVolume time = new TimeVolume(
						 rnd.nextDouble() * 20 + 20, TimeUnitEnum.Minute);
				processTime.addProcessTime(resource, time);
			}
			activity.setProcessTime(processTime);
		}
		product.setActivities(activities);

		return product;

	}

}
