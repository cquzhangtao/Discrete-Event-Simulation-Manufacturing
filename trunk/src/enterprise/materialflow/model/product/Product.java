package enterprise.materialflow.model.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.BasicNetworkEntity;
import common.NetworkUtilities;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;

public class Product extends BasicNetworkEntity<IProduct> implements IProduct {
	
	private static int count = 1;
	
	private Map<IProduct, Integer> dependenceProducts = new HashMap<IProduct, Integer>();
	private IPlant plant;
	private Map<String, IProcessActivity> activities = new HashMap<String, IProcessActivity>();

	public Product() {
		this.setName(String.valueOf(count++));
	}

	public Map<IProduct, Integer> getDependenceProducts() {
		return dependenceProducts;
	}

	public void setDependenceProducts(Map<IProduct, Integer> dependentProducts) {
		this.dependenceProducts = dependentProducts;
	}

	public IPlant getPlant() {
		return plant;
	}

	public void setPlant(IPlant plant) {
		this.plant = plant;
	}

	@Override
	public boolean addDependenceProduct(IProduct successor, int amount) {
		if(NetworkUtilities.addSuccessor(this, successor)){
			dependenceProducts.put(successor, amount);
			return true;
		}
		return false;
	}

	@Override
	public long getRawProcessingTime() {
		return 0;
	}
	
	public IProduct clone(){
		IProduct product=new Product();
		super.clone(product);
		Map<String, IProcessActivity> activities = new HashMap<String, IProcessActivity>();
		for (IProcessActivity activity : this.activities.values()) {
			IProcessActivity newAct = activity.clone();
			activities.put(newAct.getName(), newAct);
		}
		product.setActivities(activities);
		for (IActivity activity : activities.values()) {
			List<IActivity> precedesors = new ArrayList<IActivity>();
			List<IActivity> successors = new ArrayList<IActivity>();
			for (IActivity pre : activity.getPredecessors()) {
				precedesors.add(activities.get(pre.getName()));
			}
			for (IActivity suc : activity.getSuccessors()) {
				successors.add(activities.get(suc.getName()));
			}
			activity.setSuccessors(successors);
			activity.setPredecessors(precedesors);
		}
		return product;
	}

	public Map<String, IProcessActivity> getActivities() {
		return activities;
	}

	public void setActivities(Map<String, IProcessActivity> activities) {
		this.activities = activities;
	}


}
