package enterprise.materialflow.control.sequencing;

import java.util.List;

import common.Entity;
import enterprise.materialflow.model.plant.resource.IResource;
import simulation.model.activity.IActivity;

public class FIFOActivitySelector extends Entity implements IActivitySelector{

	@Override
	public IActivity selectActivity(IResource resource,List<IActivity> activities) {
		return activities.get(0);
	}

}
