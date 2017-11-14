package enterprise.materialflow.control.release;

import java.util.List;

import common.IEntity;

import enterprise.materialflow.model.plan.order.IManufactureOrder;

public interface IOrderRelease extends IEntity{
	public List<IManufactureOrder> releaseOrders(long currentTime);
	public boolean isDone();
	public long nextReleaseTime(long currentTime);
	public IOrderRelease clone();
	public List<IManufactureOrder>  getOrders();
	public void setOrders(List<IManufactureOrder> orders);

}
