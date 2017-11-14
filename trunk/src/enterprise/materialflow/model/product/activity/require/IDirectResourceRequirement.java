package enterprise.materialflow.model.product.activity.require;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;

public interface IDirectResourceRequirement extends IResourceRequirement{

	void setAlternativeResources(List<IResource> arrayList);

}
