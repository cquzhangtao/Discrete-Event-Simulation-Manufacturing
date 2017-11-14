package enterprise.materialflow.model.plan.order.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import common.IEntity;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.BasicActivityListener;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.ActivityListener;
import simulation.model.activity.IActivity;

public interface IJob extends IEntity{
	public Map<String, IProcessActivity> getActivities();
	public long getReleasedTime();
	public long getCompletedTime();
	public void addJobListener(JobListener listener);
	//public IProduct getProduct();
	public IJob clone();
	//public void setProduct(IProduct product);
	public void setReleasedTime(long releaseTime);
	public void setCompletedTime(long completedTime);
	public void setListeners(List<JobListener> arrayList);
	public void setActivities(Map<String, IProcessActivity> activities);
	public void setState(JobState state);
	public List<JobListener> getListeners();
	public void setDefaultActivityListener(
			BasicActivityListener defaultActivityListener);
	public JobState getState();
	public IManufactureOrder getOrder();


}
