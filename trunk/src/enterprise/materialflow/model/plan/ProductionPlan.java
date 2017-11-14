package enterprise.materialflow.model.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.Entity;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import simulation.model.activity.IActivity;

public class ProductionPlan extends Entity implements IProductionPlan {

	private List<IManufactureOrder> orders;
	private IOrderRelease orderRelease;

	public ProductionPlan(List<IManufactureOrder> orders) {
		this.orders = orders;
	}

	public IOrderRelease getOrderRelease() {
		return orderRelease;
	}

	public void setOrderRelease(IOrderRelease orderRelease) {
		this.orderRelease = orderRelease;
	}

	public List<IManufactureOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<IManufactureOrder> orders) {
		this.orders = orders;
	}

}
