package enterprise.materialflow.control.routing;

import java.util.List;

import common.IEntity;
import enterprise.materialflow.model.plant.resource.IResource;
import simulation.model.activity.IActivity;

public interface IResourceSelector extends IEntity{
	public IResource selectResource(IActivity activity,List<IResource>resources);
	public boolean isSelectable(IActivity activity,List<IResource>resources);

}
