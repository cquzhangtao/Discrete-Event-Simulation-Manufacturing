package enterprise.materialflow.control.release;

import java.util.ArrayList;
import java.util.List;

import common.Entity;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.Job;
import enterprise.materialflow.model.plan.order.job.JobListener;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.simulation.event.JobReleaseEvent;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import basic.volume.TimeVolume;
import simulation.core.ISimulation;

public class CONWIPJobRelease extends Entity implements IJobRelease {
	private static int num=0;
	private int targetWip;
	private int currentWip;
	private ISimulation simulation;
	private IProduct product;

	public CONWIPJobRelease(ISimulation simulation, IProduct product) {
		this.setName("Job Release "+num++);
		this.simulation = simulation;
		this.product = product;

	}

	@Override
	public List<IJob> releaseJobs(long time) {
		currentWip++;
		List<IJob> selectedJobs = new ArrayList<IJob>();
		IJob job = new Job(getProduct());
		SimulationResultNew.addProductData(product, time);
		job.setReleasedTime(time);
		selectedJobs.add(job);
		addJobListener(job);
		return selectedJobs;

	}

	private void addJobListener(IJob job) {
		job.addJobListener(new JobListener() {

			@Override
			public void onCompleted(IJob job, long time) {
				job.setCompletedTime(time);
				SimulationResultNew.addProductData(product, time, job.getCompletedTime()-job.getReleasedTime());
				currentWip--;
				for (int i = currentWip; i < targetWip; i++) {
					JobReleaseEvent event = new JobReleaseEvent(
							CONWIPJobRelease.this);
					event.setTime(time);
					simulation.addEvent(event);
				}
			}

			@Override
			public void onReleased(IJob job, long time) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public long nextReleaseTime(long currentTime) {
		return 0;
	}

	public int getTargetWip() {
		return targetWip;
	}

	public void setTargetWip(int targetWip) {
		this.targetWip = targetWip;
	}

	public IProduct getProduct() {
		return product;
	}

	public void setProduct(IProduct product) {
		this.product = product;
	}

	@Override
	public IJobRelease clone(IManufactureOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInterval(TimeVolume interval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleaseSize(int releaseSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReleasedJobNum(int releasedJobNum) {
		// TODO Auto-generated method stub
		
	}

}
