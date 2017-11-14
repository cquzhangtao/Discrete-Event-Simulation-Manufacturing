package enterprise.materialflow.model.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.Entity;

import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.resource.IResource;

public class Plant extends Entity implements IPlant{
	private Map<String,IResource> resources;
	private List<Skill> skills;


	@Override
	public Map<String,IResource> getResources() {
		return resources;
	}

	@Override
	public List<Skill> getSkills() {
		return skills;
	}

	@Override
	public void setSkills(List<Skill> skills) {
		this.skills=skills;
		
	}

	@Override
	public void setResources(Map<String,IResource> resources) {
		this.resources=resources;
		
	}
	
	public IPlant clone(){
//		IPlant plant=new Plant();
//		plant.setSkills(skills);
//		Map<String,IResource> resources=new ArrayList<IResource>();
//		for(IResource resource:this.resources){
//			resources.add(resource.clone());
//		}
//		plant.setResources(resources);
		return null;
	}
	


}
