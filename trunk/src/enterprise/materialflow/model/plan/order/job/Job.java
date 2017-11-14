package enterprise.materialflow.model.plan.order.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctc.wstx.dtd.DefaultAttrValue;

import common.Entity;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.BasicActivityListener;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import simulation.model.activity.ActivityListener;
import simulation.model.activity.IActivity;

public class Job extends Entity implements IJob {

	private static int count = 0;
	
	private IManufactureOrder order;
	private long releaseTime;
	private long completedTime;
	private Map<String, IProcessActivity> activities;
	private List<JobListener> listeners = new ArrayList<JobListener>();
	private JobState state = JobState.waiting;
	private BasicActivityListener defaultActivityListener;

	Job() {
	}
	
	public Job(IManufactureOrder order){
		this(order.getProduct());
		this.setOrder(order);
		listeners.add(order.getDefaultJobListener());
	}

	public Job(IProduct product) {
		setName("Job" + count);
		setId(String.valueOf(count));
		count++;
		activities = JobUtilities.generateActivities(this, product);
		defaultActivityListener=new BasicActivityListener(this);
		addActivityListener(activities,defaultActivityListener);
	}

	private void addActivityListener(Map<String, IProcessActivity> activities,
			BasicActivityListener defaultActivityListener) {
		for (IActivity activity : activities.values()) {
			if (!activity.getSuccessors().isEmpty()) {
				continue;
			}
			activity.addActivityListener(defaultActivityListener);
		}
	}

	@Override
	public Map<String, IProcessActivity> getActivities() {
		return activities;
	}

	public long getReleasedTime() {
		return releaseTime;
	}

	public void setReleasedTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Override
	public void addJobListener(JobListener listener) {
		listeners.add(listener);
	}

	public long getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(long completeTime) {
		this.completedTime = completeTime;
	}

	public JobState getState() {
		return state;
	}

	public void setState(JobState state) {
		this.state = state;
	}

	public IJob clone() {
		IJob job = new Job();
		super.clone(job);
		job.setReleasedTime(releaseTime);
		job.setCompletedTime(completedTime);
		job.setState(state);

		Map<String, IProcessActivity> activities = new HashMap<String, IProcessActivity>();
		for (IProcessActivity activity : this.activities.values()) {
			IProcessActivity newAct = activity.clone();
			activities.put(newAct.getName(), newAct);
		}
		job.setActivities(activities);
		
		BasicActivityListener defaultActivityListener=new BasicActivityListener();
		defaultActivityListener.setJob(job);
		job.setDefaultActivityListener(defaultActivityListener);		
		List<IProcessActivity> unfinActs = new ArrayList<IProcessActivity>();
		for (IProcessActivity activity : this.defaultActivityListener.getUnfinishedLastActivities()) {
			unfinActs.add(activities.get(activity.getName()));
		}
		defaultActivityListener.setUnfinishedLastActivities(unfinActs);
		
		for (IProcessActivity activity : activities.values()) {
			List<IActivity> precedesors = new ArrayList<IActivity>();
			List<IActivity> successors = new ArrayList<IActivity>();
			for (IActivity pre : activity.getPredecessors()) {
				precedesors.add(activities.get(pre.getName()));
			}
			for (IActivity suc : activity.getSuccessors()) {
				successors.add(activities.get(suc.getName()));
			}
			activity.setSuccessors(successors);
			activity.setPredecessors(precedesors);
			activity.getListeners().clear();
		}
		addActivityListener(activities,defaultActivityListener);


		return job;
	}

	public List<JobListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<JobListener> listeners) {
		this.listeners = listeners;
	}

	public long getReleaseDTime() {
		return releaseTime;
	}

	public void setActivities(Map<String, IProcessActivity> activities) {
		this.activities = activities;
	}

	public BasicActivityListener getDefaultActivityListener() {
		return defaultActivityListener;
	}

	public void setDefaultActivityListener(
			BasicActivityListener defaultActivityListener) {
		this.defaultActivityListener = defaultActivityListener;
	}

	public IManufactureOrder getOrder() {
		return order;
	}

	public void setOrder(IManufactureOrder order) {
		this.order = order;
	}



	

}
