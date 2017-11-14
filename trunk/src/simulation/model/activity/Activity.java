package simulation.model.activity;

import java.util.ArrayList;
import java.util.List;

import common.NetworkUtilities;
import common.TimeIntervalEntity;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.core.event.ISimulationEvent;
import simulation.model.event.ActivityEndEvent;

public class Activity extends TimeIntervalEntity<IActivity> implements
		IActivity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int count = 0;

	private List<ActivityListener> listeners = new ArrayList<ActivityListener>();
	private ActivityState state = ActivityState.waiting;
	private List<IActivityStartCondition> startConditions = new ArrayList<IActivityStartCondition>();

	public Activity() {
		super();
		setName("Activity" + count++);
		setId(String.valueOf(count));
	}
	
	public Activity(boolean clone){
		super(clone);
	}

	public List<ActivityListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<ActivityListener> listeners) {
		this.listeners = listeners;
	}

	public List<IActivityStartCondition> getStartConditions() {
		return startConditions;
	}

	public void setStartConditions(List<IActivityStartCondition> startConditions) {
		this.startConditions = startConditions;
	}

	@Override
	public List<ISimulationEvent> end(long time) {
		this.setEndTime(time);
		state = ActivityState.finished;
		for (ActivityListener lis : listeners) {
			lis.onEnd(this, time);
		}
		return new ArrayList<ISimulationEvent>();
	}

	@Override
	public List<ISimulationEvent> start(long time) {

		setStartTime(time);
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		state = ActivityState.started;
		for (ActivityListener lis : listeners) {
			lis.onStart(this, time);
		}
		ActivityEndEvent event = new ActivityEndEvent();
		event.setActivity(this);
		event.setTime(time + this.getDuration());
		events.add(event);
		return events;
	}

	public void addListener(ActivityListener lis) {
		listeners.add(lis);
	}

	@Override
	public boolean isFinished() {
		return state == ActivityState.finished;
	}

	public ActivityState getState() {
		return state;
	}

	public void setState(ActivityState state) {
		this.state = state;
	}

	@Override
	public boolean isStartable() {
		boolean startable = true;
		for (IActivityStartCondition condition : startConditions) {
			startable = startable && condition.isSatisfied(this);
		}
		return startable;
	}

	public void addStartCondition(IActivityStartCondition condition) {
		startConditions.add(condition);
	}

	public boolean addPredecessor(IActivity predecessor) {
		return NetworkUtilities.addPredecessor(this, predecessor);
	}

	public void clone(Activity activity) {
		activity.setState(state);
		activity.setStartConditions(startConditions);
		super.clone(activity);
	}

	public IActivity clone() {
		IActivity act = new Activity(true);
		clone(act);
		return act;
	}

	@Override
	public boolean isPredecessor(IActivity activity) {
		return getPredecessors().contains(activity);
	}

	@Override
	public boolean isSuccessor(IActivity activity) {

		return getSuccessors().contains(activity);
	}

	public void addActivityListener(ActivityListener lis) {
		listeners.add(lis);
	}


}
