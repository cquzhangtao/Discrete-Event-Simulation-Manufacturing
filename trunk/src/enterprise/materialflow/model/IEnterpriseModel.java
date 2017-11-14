package enterprise.materialflow.model;

import java.util.List;

import common.IEntity;

import enterprise.materialflow.model.plan.IProductionPlan;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.product.IProduct;

public interface IEnterpriseModel extends IEntity{
	public IProductionPlan getProductionPlan();
	public List<IProduct> getProducts();
	public List<IPlant> getPlants();
	public void setProducts(List<IProduct> products);
	public void setProductionPlan(IProductionPlan plan);
	public void setPlants(List<IPlant> plants);

}
