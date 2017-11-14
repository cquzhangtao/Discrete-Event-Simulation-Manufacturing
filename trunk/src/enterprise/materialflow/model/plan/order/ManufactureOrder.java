package enterprise.materialflow.model.plan.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Entity;

import enterprise.materialflow.control.release.BasicJobRelease;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.model.plan.order.job.BasicJobListener;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.Job;
import enterprise.materialflow.model.product.IProduct;
import basic.distribution.Distribution;
import basic.distribution.DistributionParameterEnum;
import basic.distribution.TimePoint;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class ManufactureOrder extends Entity implements IManufactureOrder{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3752157661235472186L;
	private static int count=0;
	private IProduct product;
	private int amount;	
	private TimePoint duedate;
	private TimePoint releaseTime;
	private IJobRelease jobRelease;
	private IManufactureOrder parent;
	private Map<String,IJob> onlineJobs=new HashMap<String,IJob>();
	private List<IJob> finishedJobs=new ArrayList<IJob>();
	private BasicJobListener defaultJobListener;
	
	protected ManufactureOrder(){
		setDefaultJobListener(new BasicJobListener(this));
	}
	
	public ManufactureOrder(IProduct product){
		setName("Order"+count++);
		this.product=product;
		jobRelease=new BasicJobRelease(this);
		setDefaultJobListener(new BasicJobListener(this));
	}
	
	public IProduct getProduct() {
		return product;
	}
	public void setProduct(IProduct product) {
		this.product = product;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public TimePoint getDuedate() {
		return duedate;
	}
	public void setDuedate(TimePoint duedate) {
		this.duedate = duedate;
	}
	public long getReleaseTime() {
		return releaseTime.nextLong();
	}
	public void setReleaseTime(TimePoint releasedate) {
		this.releaseTime = releasedate;
	}
	public IJobRelease getJobRelease() {
		return jobRelease;
	}
	public void setJobRelease(IJobRelease jobRelease) {
		this.jobRelease = jobRelease;
	}
	
	public IManufactureOrder getParent() {
		return parent;
	}
	public void setParent(IManufactureOrder parent) {
		this.parent = parent;
	}
	@Override
	public Map<String,IJob> getOnlineJobs() {
		return onlineJobs;
	}

	public void setOnlineJobs(Map<String,IJob> jobs) {
		this.onlineJobs = jobs;
	}
	
	public IManufactureOrder clone(){
		IManufactureOrder order=new ManufactureOrder();
		clone(order);
		return order;
	}
	
	public void clone(IManufactureOrder order){
		order.setProduct(product);
		order.setAmount(amount);
		order.setReleaseTime(releaseTime);
		order.setDuedate(duedate);
		order.setJobRelease(jobRelease.clone(order));
		order.setParent(parent);
		Map<String,IJob> jobs=new HashMap<String,IJob>();
		for (IJob job:this.onlineJobs.values()){
			IJob newjob = job.clone();
			newjob.addJobListener(order.getDefaultJobListener());
			jobs.put(newjob.getName(),newjob);
		}
		order.setOnlineJobs(jobs);
		super.clone(order);		
	}

	public BasicJobListener getDefaultJobListener() {
		return defaultJobListener;
	}

	public void setDefaultJobListener(BasicJobListener defaultJobListener) {
		this.defaultJobListener = defaultJobListener;
	}

	public List<IJob> getFinishedJobs() {
		return finishedJobs;
	}

	public void setFinishedJobs(List<IJob> finishedJobs) {
		this.finishedJobs = finishedJobs;
	}

}
