package enterprise.materialflow.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.IEntity;
import common.INetworkEntity;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;

public interface IProduct extends IEntity,INetworkEntity<IProduct> {
	
	public Map<IProduct,Integer> getDependenceProducts();
	public void setActivities(Map<String, IProcessActivity> activities);
	public void setPlant(IPlant plant);
	public boolean addDependenceProduct(IProduct product,int amount);
	public Map<String,IProcessActivity> getActivities();
	public long getRawProcessingTime();
	public IProduct clone();

}
