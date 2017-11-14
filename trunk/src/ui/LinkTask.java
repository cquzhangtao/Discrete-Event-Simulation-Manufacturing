package ui;


import java.util.ArrayList;
import java.util.List;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.TimePeriod;


public class LinkTask extends Task{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LinkTask> successors;
	private List<LinkTask> predecessors;
	private TimeSlice timeSlice;
	
	public LinkTask(TimeSlice timeSlice, TimePeriod duration) {
		super(timeSlice.getState().getName(), duration);
		successors=new ArrayList<LinkTask>();
		predecessors=new ArrayList<LinkTask>();
		this.timeSlice=timeSlice;
	}

	public void addSuccessor(LinkTask successor){
		if(!successors.contains(successor)&&successor!=null){
			successors.add(successor);
		successor.addPredecessor(this);
		}
	}
	
	public void addPredecessor(LinkTask predecessor){
		if(!predecessors.contains(predecessor)&&predecessor!=null){
			predecessors.add(predecessor);
		predecessor.addSuccessor(this);
		}
	}

	public List<LinkTask> getSuccessors() {
		return successors;
	}


	public List<LinkTask> getpredecessors() {
		return predecessors;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}

}
