package common;


public interface ITimeInterval {

	public abstract long getStartTime();

	public abstract long getEndTime();

	public abstract void setStartTime(long startTime);

	public abstract void setEndTime(long endtime);
	
	public abstract long getDuration();
	
	public abstract boolean contains(long time);

}