package enterprise.materialflow.control.release;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.plan.IProductionPlan;
import enterprise.materialflow.model.plan.order.IManufactureOrder;

public class BasicOrderRelease extends Entity implements IOrderRelease{
	
	private List<IManufactureOrder> orders;
	
	public BasicOrderRelease(List<IManufactureOrder> orders){
		this.orders=new ArrayList<IManufactureOrder>(orders);
	}

	private BasicOrderRelease() {
	}

	@Override
	public List<IManufactureOrder> releaseOrders(long time) {
		List<IManufactureOrder> selectedOrders=new  ArrayList<IManufactureOrder>();
		for(IManufactureOrder order:orders){
			if(order.getReleaseTime()<=time){
				selectedOrders.add(order);
				
			}
		}
		orders.removeAll(selectedOrders);
		return selectedOrders;
	}

	@Override
	public boolean isDone() {
		return orders.isEmpty();
	}

	@Override
	public long nextReleaseTime(long currentTime) {
		return getEarliestOrder().getReleaseTime();
	}
	
	private IManufactureOrder getEarliestOrder(){
		IManufactureOrder eorder=orders.get(0);
		for(IManufactureOrder order:orders){
			if(eorder.getReleaseTime()>order.getReleaseTime()){
				eorder=order;
			}
		}
		return eorder;
	}
	
	public IOrderRelease clone(){
		return new BasicOrderRelease();
	}

	@Override
	public List<IManufactureOrder> getOrders() {
		return orders;
	}

	@Override
	public void setOrders(List<IManufactureOrder> orders) {
		this.orders=orders;
		
	}

	

}
