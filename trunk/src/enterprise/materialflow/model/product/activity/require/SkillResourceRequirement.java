package enterprise.materialflow.model.product.activity.require;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.Department;
import enterprise.materialflow.model.plant.resource.IResource;


public class SkillResourceRequirement extends Entity implements ISkillResourceRequirement{
	
	private static int num=0;
	
	public SkillResourceRequirement(){
		super();
		setName("ResReq"+num++);
	}
	public SkillResourceRequirement(boolean clone){
		super(clone);
	}
	
	private Skill neededSkill;
	private Department department;	
	private List<IResource> allResources;
	private List<IResource> alternativeResources;
	
	public List<IResource> getAllResources() {
		return allResources;
	}

	public void setAllResources(List<IResource> allResources) {
		this.allResources = allResources;
	}

	public Skill getNeededSkill() {
		return neededSkill;
	}

	public void setNeededSkill(Skill neededSkill) {
		this.neededSkill = neededSkill;
	}

	@Override
	public List<IResource> getAlternativeResources() {
		if(alternativeResources!=null){
			return alternativeResources;
		}
		alternativeResources=new ArrayList<IResource>();
		for(IResource res:allResources){
			if(/*res.getDepartment()==department&&*/res.getSkills().contains(neededSkill)){
				alternativeResources.add(res);
			}
		}
		return alternativeResources;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public ISkillResourceRequirement clone(){
		ISkillResourceRequirement req=new SkillResourceRequirement(true);
		req.setNeededSkill(neededSkill);
		req.setDepartment(department);
		req.setAllResources(allResources);
		super.clone(req);
		return req;
	}

	
	
	

}
