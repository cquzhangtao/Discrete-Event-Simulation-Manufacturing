package enterprise.materialflow.model.plan.order;

import java.util.List;
import java.util.Map;

import common.IEntity;

import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.model.plan.order.job.BasicJobListener;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.product.IProduct;
import basic.distribution.TimePoint;

public interface IManufactureOrder extends IEntity{
	
	public IProduct getProduct();
	
	public void setProduct(IProduct product);

	public void setParent(IManufactureOrder parent);
	
	public long getReleaseTime();
	
	public void setReleaseTime(TimePoint time);
	
	public Map<String,IJob> getOnlineJobs();
	
	public void setJobRelease(IJobRelease release);
	
	public IJobRelease getJobRelease();

	public IManufactureOrder clone();
	
	public int getAmount();
	
	public void setAmount(int amount);

	public void setDuedate(TimePoint duedate);

	public void setOnlineJobs(Map<String,IJob> jobs);

	public IManufactureOrder getParent();
	
	public BasicJobListener getDefaultJobListener() ;

	public List<IJob> getFinishedJobs();
	

}
