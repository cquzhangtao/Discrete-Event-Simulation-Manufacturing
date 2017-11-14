package enterprise.materialflow.control.sequencing;

import java.util.List;

import common.IEntity;
import enterprise.materialflow.model.plant.resource.IResource;
import simulation.model.activity.IActivity;

public interface IActivitySelector extends IEntity{
	public IActivity selectActivity(IResource resource,List<IActivity> activities);
}
