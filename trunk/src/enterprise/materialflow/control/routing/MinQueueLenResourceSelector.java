package enterprise.materialflow.control.routing;

import java.util.ArrayList;
import java.util.List;

import common.Entity;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.ResourceState;
import simulation.model.activity.IActivity;

public class MinQueueLenResourceSelector extends Entity implements IResourceSelector{

	@Override
	public IResource selectResource(IActivity activity,List<IResource> resources) {
		List<IResource> seleres=new ArrayList<IResource>();
		for(IResource res:resources){
			if(res.getState()==ResourceState.idle){
				seleres.add(res);
			}
		}
		if(seleres.isEmpty()){
			return getMinQueueResource(resources);
		}
		
		return getMinQueueResource(seleres);
	}

	@Override
	public boolean isSelectable(IActivity activity,List<IResource> resources) {
		return true;
	}
	
	private IResource getMinQueueResource(List<IResource> resources){
		IResource min=resources.get(0);
		for(IResource res:resources){
			if(min.getQueue().size()>res.getQueue().size()){
				min=res;
			}
		}
		return min;
		
	}

}
