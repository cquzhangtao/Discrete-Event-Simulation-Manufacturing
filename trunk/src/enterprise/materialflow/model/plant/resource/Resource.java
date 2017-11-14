package enterprise.materialflow.model.plant.resource;

import java.util.ArrayList;
import java.util.List;

import common.Entity;
import enterprise.materialflow.control.sequencing.FIFOActivitySelector;
import enterprise.materialflow.control.sequencing.IActivitySelector;
import enterprise.materialflow.control.sequencing.STTDActivitySelector;
import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.Department;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.event.ResourceSelectingActivityEvent;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public class Resource extends Entity implements IResource {

	private static int count = 0;

	private ResourceState state = ResourceState.idle;
	private List<Skill> skills;
	private Department department;
	private List<IActivity> queue = new ArrayList<IActivity>();
	private IActivitySelector activitySelector;

	public Resource() {
		super();
		skills = new ArrayList<Skill>();
		this.setName("R" + count++);
		activitySelector = new FIFOActivitySelector();

	}

	public ResourceState getState() {
		return state;
	}

	public void setState(ResourceState state) {
		this.state = state;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public List<IActivity> getQueue() {
		return queue;
	}

	public void setQueue(List<IActivity> queue) {
		this.queue = queue;
	}

	public void addSkill(Skill skill) {
		if (!skills.contains(skill))
			skills.add(skill);
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public List<ISimulationEvent> free(IActivity activity, long time) {
		state = ResourceState.idle;
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (queue.size() > 0) {
			events.addAll(start(time));
		}
		return events;
	}

	@Override
	public List<ISimulationEvent> occupy(IActivity activity, long time) {
		return null;

	}

	@Override
	public List<ISimulationEvent> queue(IActivity activity, long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		queue.add(activity);
		if (state == ResourceState.idle) {
			events.addAll(start(time));
		}
		return events;

	}

	protected List<ISimulationEvent> start(long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (queue.isEmpty()) {
			return events;
		}
		if (this.getActivitySelector() instanceof STTDActivitySelector) {
			ResourceSelectingActivityEvent event = new ResourceSelectingActivityEvent();
			event.setResource(this);
			event.setTime(time);
			events.add(event);
		} else {
			IActivity act = activitySelector.selectActivity(this, queue);
			if (act == null) {
				return events;
			}			
			events.addAll(start(act, time));
		}
		return events;
	}

	public List<ISimulationEvent> start(IActivity act, long time) {
		queue.remove(act);
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		state = ResourceState.occupied;
		events.addAll(act.start(time));
		return events;
	}

	public IActivitySelector getActivitySelector() {
		return activitySelector;
	}

	public void setActivitySelector(IActivitySelector activitySelector) {
		this.activitySelector = activitySelector;
	}

	public IResource clone() {
		IResource resource = new Resource();
		clone(resource);
		return resource;
	}

	public void clone(IResource resource) {
		resource.setState(state);
		resource.setSkills(skills);
		resource.setDepartment(department);
		resource.setQueue(new ArrayList<IActivity>(queue));
		super.clone(resource);
	}

	@Override
	public List<ISimulationEvent> selectActivity(long time) {

		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (state != ResourceState.idle) {
			return events;
		}
		if (queue.isEmpty()) {
			return events;
		}
		IActivity act = activitySelector.selectActivity(this, queue);
		if (act == null) {
			return events;
		}
		events.addAll(start(act, time));
		return events;
	}

}
