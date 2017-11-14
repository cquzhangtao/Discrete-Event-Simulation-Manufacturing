package enterprise.materialflow.model.product.activity.require;

import java.util.ArrayList;
import java.util.List;

import common.IEntity;

import enterprise.materialflow.model.plant.resource.IResource;

public interface IResourceRequirement extends IEntity{
	public List<IResource> getAlternativeResources();
	public IResourceRequirement clone();



	
}
