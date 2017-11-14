package enterprise.materialflow.optimization.ga;
import java.util.ArrayList;
import java.util.List;

import simulation.model.activity.IActivity;
import common.NetworkUtilities;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.model.product.activity.ProcessActivity;


public class ActivityEx  implements Comparable<ActivityEx>{

	private List<IActivity> aidPredecessors=new ArrayList<IActivity>();
	private List<IActivity> aidSuccessors=new ArrayList<IActivity>();
	private List<IActivity> aidPredecessorsBack=new ArrayList<IActivity>();
	private List<IActivity> aidSuccessorsBack=new ArrayList<IActivity>();
	private int positionOnResource;
	private int positionOnResourceBack;
	private IResource assignedResourceBack;	
	private IProcessActivity activity;
	
	public ActivityEx(IProcessActivity activity){
		this.setActivity(activity);
		
	}
	
	public List<IActivity> getAidPredecessors() {
		return aidPredecessors;
	}

	public void setAidPredecessors(List<IActivity> aidPredecessors) {
		this.aidPredecessors = aidPredecessors;
	}
			
	public boolean addAidPredecessor(ActivityEx predecessor){
		if(this!=predecessor&&!NetworkUtilities.inDownstream(activity,predecessor.getActivity())&&!NetworkUtilities.inUpstream(predecessor.getActivity(),activity)){
			if(!activity.getPredecessors().contains(predecessor.getActivity())){
				activity.getPredecessors().add(predecessor.getActivity());
				aidPredecessors.add(predecessor.getActivity());
			}
			if(!predecessor.getActivity().getSuccessors().contains(activity)){
				predecessor.getActivity().getSuccessors().add(activity);
				predecessor.getAidSuccessors().add(activity);
			}
			return true;
		}
		return false;
	}
	
	public boolean addPredecessorFormCycle(ActivityEx predecessor){
		return !(this!=predecessor&&!NetworkUtilities.inDownstream(activity,predecessor.getActivity())&&!NetworkUtilities.inUpstream(predecessor.getActivity(),activity));
			
			
	}
	
	
	
	
	@Override
	public int compareTo(ActivityEx o) {
		
		return getPositionOnResource()-o.getPositionOnResource();
		
	}

	public List<IActivity> getAidPredecessorsBack() {
		return aidPredecessorsBack;
	}

	public void setAidPredecessorsBack(List<IActivity> aidPredecessorsBack) {
		this.aidPredecessorsBack = aidPredecessorsBack;
	}

	public List<IActivity> getAidSuccessorsBack() {
		return aidSuccessorsBack;
	}

	public void setAidSuccessorsBack(List<IActivity> aidSuccessorsBack) {
		this.aidSuccessorsBack = aidSuccessorsBack;
	}

	public int getPositionOnResourceBack() {
		return positionOnResourceBack;
	}

	public void setPositionOnResourceBack(int positionOnResourceBack) {
		this.positionOnResourceBack = positionOnResourceBack;
	}

	public IResource getAssignedResourceBack() {
		return assignedResourceBack;
	}

	public void setAssignedResourceBack(IResource assignedResourceBack) {
		this.assignedResourceBack = assignedResourceBack;
	}

	public int getPositionOnResource() {
		return positionOnResource;
	}

	public void setPositionOnResource(int positionOnResource) {
		this.positionOnResource = positionOnResource;
	}

	public List<IActivity> getAidSuccessors() {
		return aidSuccessors;
	}

	public void setAidSuccessors(List<IActivity> aidSuccessors) {
		this.aidSuccessors = aidSuccessors;
	}
	public IProcessActivity getActivity() {
		return activity;
	}
	public void setActivity(IProcessActivity activity) {
		this.activity = activity;
	}
	
	
	
}
