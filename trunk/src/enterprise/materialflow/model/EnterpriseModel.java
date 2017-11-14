package enterprise.materialflow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.Entity;
import enterprise.materialflow.model.plan.IProductionPlan;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.IProduct;
import simulation.model.activity.IActivity;

public class EnterpriseModel extends Entity implements IEnterpriseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<IPlant> plants;
	private List<IProduct> products;
	private IProductionPlan productionPlan;	
	private EnterpriseModel(){
		
	}
	
	public EnterpriseModel(List<IPlant> plants,List<IProduct> products,IProductionPlan productionPlan){
		this.plants=plants;
		this.products=products;
		this.productionPlan=productionPlan;
	}
	
	public List<IPlant> getPlants() {
		return plants;
	}
	public void setPlants(List<IPlant> plants) {
		this.plants = plants;
	}
	public List<IProduct> getProducts() {
		return products;
	}
	public void setProducts(List<IProduct> products) {
		this.products = products;
	}
	
	@Override
	public IProductionPlan getProductionPlan() {
		return productionPlan;
	}
	public void setProductionPlan(IProductionPlan productionPlan) {
		this.productionPlan = productionPlan;
	}
}
