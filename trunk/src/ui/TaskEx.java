package ui;
import java.util.Date;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

import simulation.model.activity.IActivity;


public class TaskEx extends Task implements Comparable<Task>{
	private boolean bold;
	private boolean alternativePosition;
	private boolean precedenceActivity;
	private boolean sucessorActivity;
	private boolean currentActivity;
	private boolean sameJob;
	private IActivity activity;
	public TaskEx(String description, Date start, Date end) {
		super(description, start, end);
	}

	public TaskEx(String description, SimpleTimePeriod p) {
		super(description,p);
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public IActivity getActivity() {
		return activity;
	}

	public void setActivity(IActivity activity) {
		this.activity = activity;
	}

	@Override
	public int compareTo(Task o) {
		return (int) (this.getDuration().getStart().getTime()-o.getDuration().getStart().getTime());
	}

	public boolean isAlternativePosition() {
		return alternativePosition;
	}

	public void setAlternativePosition(boolean alternativePosition) {
		this.alternativePosition = alternativePosition;
	}

	public boolean isPrecedenceActivity() {
		return precedenceActivity;
	}

	public void setPrecedenceActivity(boolean precedenceActivity) {
		this.precedenceActivity = precedenceActivity;
	}

	public boolean isCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(boolean currentActivity) {
		this.currentActivity = currentActivity;
	}

	public boolean isSucessorActivity() {
		return sucessorActivity;
	}

	public void setSucessorActivity(boolean sucessorActivity) {
		this.sucessorActivity = sucessorActivity;
	}

	public boolean isSameJob() {
		return sameJob;
	}

	public void setSameJob(boolean sameJob) {
		this.sameJob = sameJob;
	}



	



	
}
