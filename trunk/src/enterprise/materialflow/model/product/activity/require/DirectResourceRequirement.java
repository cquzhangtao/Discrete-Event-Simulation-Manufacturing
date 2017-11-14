package enterprise.materialflow.model.product.activity.require;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.plant.resource.IResource;

public class DirectResourceRequirement extends Entity implements IDirectResourceRequirement{
	
	List<IResource> alternativeResources;

	@Override
	public List<IResource> getAlternativeResources() {
		return alternativeResources;
	}

	public void setAlternativeResources(List<IResource> alternativeResources) {
		this.alternativeResources = alternativeResources;
	}

	public IResourceRequirement clone(){
		IDirectResourceRequirement req=new DirectResourceRequirement();
		req.setAlternativeResources(new ArrayList<IResource>(alternativeResources));
		return req;
	}
}
