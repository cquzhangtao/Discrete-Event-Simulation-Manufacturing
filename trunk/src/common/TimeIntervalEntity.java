package common;


public class TimeIntervalEntity<T> extends BasicNetworkEntity<T> implements ITimeInterval{

	private long startTime,endTime;
	private long earliestStartTime,earliestEndTime;
	private long latestStartTime,latestEndTime;
	
	public TimeIntervalEntity(){
		super();
	}
	public TimeIntervalEntity(boolean clone){
		super(clone);
	}
	public TimeIntervalEntity(long start,long end){
		this.startTime=start;
		this.endTime=end;
	}
	public long getEarliestStartTime() {
		return earliestStartTime;
	}
	public void setEarliestStartTime(long earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
	}
	public long getEarliestEndTime() {
		return earliestEndTime;
	}
	public void setEarliestEndTime(long earliestEndTime) {
		this.earliestEndTime = earliestEndTime;
	}
	public long getLatestStartTime() {
		return latestStartTime;
	}
	public void setLatestStartTime(long latestStartTime) {
		this.latestStartTime = latestStartTime;
	}
	public long getLatestEndTime() {
		return latestEndTime;
	}
	public void setLatestEndTime(long latestEndTime) {
		this.latestEndTime = latestEndTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	@Override
	public long getDuration() {
		
		return endTime-startTime;
	}
	@Override
	public boolean contains(long time) {
		
		return time>=startTime&&time<=endTime;
	}
	
	public void clone(TimeIntervalEntity<T> interval){
		interval.setStartTime(startTime);
		interval.setEndTime(endTime);
		super.clone(interval);
	}

}
