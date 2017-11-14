package enterprise.materialflow.control.release;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.Job;
import enterprise.materialflow.model.plan.order.job.JobListener;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class BasicJobRelease extends Entity implements IJobRelease{
	private static int num=0;
	private TimeVolume interval;
	private int releaseSize=1;
	private int releasedJobNum=0;
	private IManufactureOrder order;
	
	public BasicJobRelease(IManufactureOrder order){
		this.setName("Job Release "+num++);
		this.order=order;
		interval=new TimeVolume(0,TimeUnitEnum.Hour);
	}

	@Override
	public List<IJob> releaseJobs(long time) {
		List<IJob> selectedJobs=new ArrayList<IJob>();
		int num=0;
		while(num<releaseSize&&releasedJobNum<order.getAmount()){
			num++;
			releasedJobNum++;
			IJob job=new Job(order);						
			selectedJobs.add(job);
			for(JobListener lis:job.getListeners()){
				lis.onReleased(job, time);
			}			
		}
		
		return selectedJobs;
	}

	@Override
	public boolean isDone() {
		return releasedJobNum>=order.getAmount();
	}

	@Override
	public long nextReleaseTime(long currentTime) {
		return currentTime+interval.getMilliSeconds();
	}

	public int getReleaseSize() {
		return releaseSize;
	}

	public void setReleaseSize(int releaseSize) {
		this.releaseSize = releaseSize;
	}

	public TimeVolume getInterval() {
		return interval;
	}

	public void setInterval(TimeVolume interval) {
		this.interval = interval;
	}
	
	public IJobRelease clone(IManufactureOrder order){
		
		IJobRelease release = new BasicJobRelease(order);
		super.clone(release);
		release.setInterval(interval);
		release.setReleaseSize(releaseSize);
		release.setReleasedJobNum(releasedJobNum);
		return release;
	}

	@Override
	public void setReleasedJobNum(int releasedJobNum) {
		this.releasedJobNum=releasedJobNum;
		
	}

}
