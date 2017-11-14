package enterprise.materialflow.model.plan;

import java.util.List;
import java.util.Map;

import common.IEntity;

import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;

public interface IProductionPlan extends IEntity{
	public void setOrders(List<IManufactureOrder> orders);
	public List<IManufactureOrder> getOrders();
	public void setOrderRelease(IOrderRelease orderRelease);
	public IOrderRelease getOrderRelease();	

}
