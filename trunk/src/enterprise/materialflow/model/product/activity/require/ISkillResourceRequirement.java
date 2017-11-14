package enterprise.materialflow.model.product.activity.require;

import java.util.List;

import enterprise.materialflow.model.meta.Skill;
import enterprise.materialflow.model.plant.Department;
import enterprise.materialflow.model.plant.resource.IResource;

public interface ISkillResourceRequirement extends IResourceRequirement{
	public Skill getNeededSkill();
	public void setAllResources(List<IResource> allResources);
	public void setNeededSkill(Skill neededSkill);
	public void setDepartment(Department department);
}
