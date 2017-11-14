package enterprise.materialflow.model.plant.resource;

import java.util.ArrayList;
import java.util.List;

import common.IEntity;
import enterprise.materialflow.control.sequencing.IActivitySelector;
import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.Department;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public interface IResource extends IEntity{
	public List<ISimulationEvent> free(IActivity activity,long time);
	public List<ISimulationEvent> occupy(IActivity activity,long time);
	public List<ISimulationEvent> queue(IActivity activity,long time);
	public List<Skill> getSkills();
	public Department getDepartment();
	public ResourceState getState();
	public List<IActivity> getQueue();
	public List<ISimulationEvent> start(IActivity act,long time);
	public void setActivitySelector(IActivitySelector activitySelector);
	public IResource clone();
	public void setState(ResourceState state);
	public void setSkills(List<Skill> skills);
	public void setDepartment(Department department);
	public void setQueue(List<IActivity> arrayList);
	public List<ISimulationEvent> selectActivity(long time);

}
