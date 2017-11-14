package enterprise.materialflow.model.plant;

import java.util.List;
import java.util.Map;

import common.IEntity;

import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.resource.IResource;

public interface IPlant extends IEntity{
	
	public Map<String, IResource> getResources();
	public void setResources(Map<String,IResource> resources);
	public List<Skill> getSkills();
	public void setSkills(List<Skill> skills);
	public IPlant clone();

}
