package enterprise.materialflow.simulation.event;

import java.util.ArrayList;

import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.plant.IPlant;
import enterprise.materialflow.model.plant.resource.IInterruptableResource;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;

public class InitialResourceOfflineEvents extends ArrayList<ResourceOfflineEvent>{

	public InitialResourceOfflineEvents(IEnterpriseSimulationModel model){		
			for(IResource resource:model.getResources().values()){
				IInterruptableResource res = (IInterruptableResource) resource;
				ResourceOfflineEvent event=new ResourceOfflineEvent(res);
				add(event);
				event.setTime(res.getTimeToNextOffline().getMilliSeconds());
			}
	
		
	}

}
