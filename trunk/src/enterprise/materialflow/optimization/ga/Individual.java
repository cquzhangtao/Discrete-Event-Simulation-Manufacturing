package enterprise.materialflow.optimization.ga;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;

public class Individual {
	private double fitness;
	
	private List<ActivityEx> activities;
	private List<IResource> resources;
	private List<Integer> sequence;
	private List<Integer> allocation;
	private boolean best;
	
	public List<Integer> getSequence() {
		return sequence;
	}
	public void setSequence(List<Integer> sequence) {
		this.sequence = sequence;
	}
	public List<Integer> getAllocation() {
		return allocation;
	}
	public void setAllocation(List<Integer> allocation) {
		this.allocation = allocation;
	}
	
	public void decode(){
		for(int i=0;i<allocation.size();i++){
			activities.get(i).getActivity().setAssignedResource(resources.get(allocation.get(i)));
			activities.get(i).setPositionOnResource(sequence.get(i));
		}
	}
	
	public void encode(){
		sequence=new ArrayList<Integer>();
		allocation=new ArrayList<Integer>();
		for(ActivityEx activity:activities){
			sequence.add(activity.getPositionOnResource());
			allocation.add(resources.indexOf(activity.getActivity().getAssignedResource()));
		}
	}
	

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public List<ActivityEx> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityEx> activities) {
		this.activities = activities;
	}
	public List<IResource> getResources() {
		return resources;
	}
	public void setResources(List<IResource> resources) {
		this.resources = resources;
	}
	
	public Individual clone(){
		Individual ind=new Individual();
		ind.setActivities(activities);
		ind.setResources(resources);
		ind.setSequence(new ArrayList<Integer>(sequence));
		ind.setAllocation(new ArrayList<Integer>(allocation));
		ind.setFitness(fitness);
		return ind;
	}
	public boolean isBest() {
		return best;
	}
	public void setBest(boolean best) {
		this.best = best;
	}

}
