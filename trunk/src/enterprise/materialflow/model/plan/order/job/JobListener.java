package enterprise.materialflow.model.plan.order.job;

import java.io.Serializable;


public interface JobListener extends Serializable{
	public void onCompleted(IJob job, long time);
	public void onReleased(IJob job,long time);

}
