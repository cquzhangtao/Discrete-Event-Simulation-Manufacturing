package ui;

import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;

public interface IGanntDataset {

	public List<IResource> getGanntResources();
	public List<IProcessActivity> getGanntActivities();

}
