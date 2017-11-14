package ui;


import java.util.HashMap;
import java.util.Map;

import simulation.model.activity.IActivity;


public class TimeSlice implements Comparable<TimeSlice>{
	              
	private State state;
	private long start;
	private long end;
	private long duration;
	private Map<String,Object> parameters;
	private IActivity timeSliceBelongto;
	public TimeSlice(State name, long start, long end) {
		this.parameters=new HashMap<String,Object>();
		this.state=name;
		this.start=start;
		this.end=end;
		this.setDuration(end-start);
	}
	public void addParameter(String name,Object value){
		parameters.put(name,value);
	}
	public Object getParameter(String name){
		
		return parameters.get(name);
		
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	@Override
	public int compareTo(TimeSlice o) {
		// TODO Auto-generated method stub
		return Long.compare(end, o.end);
	}
	public long getDuration() {
		// TODO Auto-generated method stub
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public IActivity getTimeSliceBelongto() {
	
		return timeSliceBelongto;
	}
	public void setTimeSliceBelongto(IActivity timeSliceBelongto) {
	
		this.timeSliceBelongto = timeSliceBelongto;
	}
}
