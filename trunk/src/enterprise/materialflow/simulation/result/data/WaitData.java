package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;

public class WaitData implements Serializable{

	private String jobName;
	private long endWaitTime;
	private long waitTime;
	public WaitData(String jobName, long endWaitTime, long waitTime) {
		this.jobName=jobName;
		this.setEndWaitTime(endWaitTime);
		this.setWaitTime(waitTime);
		
	}
	public long getEndWaitTime() {
		return endWaitTime;
	}
	public void setEndWaitTime(long endWaitTime) {
		this.endWaitTime = endWaitTime;
	}
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	

}
