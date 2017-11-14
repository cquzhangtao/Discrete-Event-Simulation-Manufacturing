package enterprise.materialflow.control.release;

import java.util.List;

import common.IEntity;

import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.job.IJob;
import basic.volume.TimeVolume;

public interface IJobRelease extends IEntity{
	public List<IJob> releaseJobs(long time);
	public boolean isDone();
	public long nextReleaseTime(long currentTime);
	public IJobRelease clone(IManufactureOrder order);
	public void setInterval(TimeVolume interval);
	public void setReleaseSize(int releaseSize);
	public void setReleasedJobNum(int releasedJobNum);

}
