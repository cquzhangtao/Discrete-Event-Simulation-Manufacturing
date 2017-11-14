package enterprise.materialflow.control.routing;

import java.util.ArrayList;
import java.util.List;

import common.Entity;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.plant.resource.ResourceState;
import simulation.model.activity.IActivity;

public class MinQueueTimeResourceSelector extends Entity implements IResourceSelector{
	
	

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
		long minTime=getQueueTime(min);
		for(IResource res:resources){
			long time=getQueueTime(res);
			if(minTime>time){
				min=res;
				minTime=time;
			}
		}
		return min;
		
	}
	private long getQueueTime(IResource resource){
		long sum=0;
		for(IActivity act:resource.getQueue()){
			sum+=act.getDuration();
		}
		return sum;
	}


}
