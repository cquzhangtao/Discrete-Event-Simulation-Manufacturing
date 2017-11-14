package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;

public class JobSubData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4320807771477246260L;
	private String resourceName;
	private long transportBeginTime;
	private long waitBeginTime;
	private long processBeginTime;
	private long blockBeginTime;
	private long nextTransportBeginTime;
	private boolean finished=false;

	JobSubData() {

	}
	
	public JobSubData clone(){
		JobSubData subData=new JobSubData();
		subData.setToolName(getResourceName());
		subData.setTransportBeginTime(transportBeginTime);
		subData.setWaitBeginTime(waitBeginTime);
		subData.setProcessBeginTime(processBeginTime);
		subData.setBlockBeginTime(blockBeginTime);
		subData.setNextTransportBeginTime(nextTransportBeginTime);
		subData.setFinished(finished);
		return subData;
	}

	JobSubData(String s) {
		String temp[] = s.split("-");
		int i = 0;
		setToolName(temp[i++]);
		setTransportBeginTime(Long.valueOf(temp[i++]));
		setWaitBeginTime(Long.valueOf(temp[i++]));
		setProcessBeginTime(Long.valueOf(temp[i++]));
		setBlockBeginTime(Long.valueOf(temp[i++]));
		setNextTransportBeginTime(Long.valueOf(temp[i++]));
	}

	@Override
	public String toString() {
		return String.format("%s-%d-%d-%d-%d-%d", getToolName(), getTransportBeginTime(),
				getWaitBeginTime(), getProcessBeginTime(), getBlockBeginTime(),
				getNextTransportBeginTime());
	}

	public long getTransportBeginTime() {
		return transportBeginTime;
	}

	public void setTransportBeginTime(long transportBeginTime) {
		this.transportBeginTime = transportBeginTime;
	}

	public long getWaitBeginTime() {
		return waitBeginTime;
	}

	public void setWaitBeginTime(long waitBeginTime) {
		this.waitBeginTime = waitBeginTime;
	}

	public long getProcessBeginTime() {
		return processBeginTime;
	}

	public void setProcessBeginTime(long processBeginTime) {
		this.processBeginTime = processBeginTime;
	}

	public long getBlockBeginTime() {
		return blockBeginTime;
	}

	public void setBlockBeginTime(long blockBeginTime) {
		this.blockBeginTime = blockBeginTime;
	}

	public long getNextTransportBeginTime() {
		return nextTransportBeginTime;
	}

	public void setNextTransportBeginTime(long nextTransportBeginTime) {
		this.nextTransportBeginTime = nextTransportBeginTime;
	}

	public String getToolName() {
		return getResourceName();
	}

	public void setToolName(String toolName) {
		this.setResourceName(toolName);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}
