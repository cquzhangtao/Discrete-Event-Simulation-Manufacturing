package com.tao.fab.sim.event;

import java.util.ArrayList;
import java.util.List;

public class ResourceGroup implements IResourceGroup{

	private JobType jobType;
	private List<IResource> resources;
	private List<IJob> frontQueue=new ArrayList<IJob>();
	private JobType jobTypeInFrontQueue;
	private List<IJob> frontQueueWithOrganizedJobs=new ArrayList<IJob>();
	
	@Override
	public JobType getJobType() {

		return jobType;
	}

	@Override
	public List<IResource> getResources() {
		return resources;
	}

	@Override
	public boolean hasIdleResource() {
		for(IResource res:resources){
			if(!res.isSeized()){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IJob> getFrontQueue() {
		return frontQueue;
	}

	@Override
	public List<IJob> getFrontQueueWithOrganizedJobs() {
		return frontQueueWithOrganizedJobs;
	}

	@Override
	public List<IResource> getIdleResources() {
		List<IResource>ress=new ArrayList<IResource>();
		for(IResource res:resources){
			if(!res.isSeized()){
				 ress.add(res);
			}
		}
		return ress;
	}

	@Override
	public JobType getJobTypeInQueue() {
		return jobTypeInFrontQueue;
	}

	@Override
	public void setResourceNumber(int num) {
		resources=new ArrayList<IResource>();
		for(int i=0;i<num;i++){
			IResource res = new Resource();
			res.setToolGroup(this);
			resources.add(res);
		}
		
	}

}
