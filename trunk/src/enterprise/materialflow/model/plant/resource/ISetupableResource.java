package enterprise.materialflow.model.plant.resource;

import java.util.List;

import enterprise.materialflow.model.plant.resource.setup.ISetupTime;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public interface ISetupableResource extends IResource{

	public List<ISimulationEvent> endSetup(long currentTime);
	public void setSetupTime(ISetupTime setupTime);
	public void setPreActivity(IActivity preActivity);
	public void setWaitingSetupActivity(IActivity waitingSetupActivity);

}
