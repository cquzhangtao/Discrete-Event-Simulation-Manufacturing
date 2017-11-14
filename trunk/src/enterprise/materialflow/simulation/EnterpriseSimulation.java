package enterprise.materialflow.simulation;

import java.util.List;

import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.event.JobReleaseEvent;
import enterprise.materialflow.simulation.event.OrderReleaseEvent;
import enterprise.materialflow.simulation.event.ResourceRelatedEvent;
import enterprise.materialflow.simulation.model.EnterpriseSimulationlModelUtilities;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.core.ISimulation;
import simulation.core.Simulation;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;
import simulation.model.event.ActivityRelatedEvent;

public class EnterpriseSimulation extends Simulation implements
		IEnterpriseSimulation {
	private IEnterpriseSimulationModel enterpriseSimulationModel;

	private EnterpriseSimulation() {

	}

	public EnterpriseSimulation(IEnterpriseSimulationModel enterprise) {
		this.enterpriseSimulationModel = enterprise;
		addInitialEvents();
	}

	private void addInitialEvents() {
		addEvent(new OrderReleaseEvent(
				enterpriseSimulationModel.getOrderRelease()));
		// this.getEventList().addAll(new
		// InitialResourceOfflineEvents(enterprise));
	}

	public IEnterpriseSimulationModel getEnterpriseSimulationModel() {
		return enterpriseSimulationModel;
	}

	public void setEnterpriseSimulationModel(
			IEnterpriseSimulationModel enterpriseSimulationModel) {
		this.enterpriseSimulationModel = enterpriseSimulationModel;
	}

	public IEnterpriseSimulation clone() {
		IEnterpriseSimulation simulation = new EnterpriseSimulation();
		super.clone(simulation);
		IEnterpriseSimulationModel newModel = enterpriseSimulationModel.clone();
		simulation.setEnterpriseSimulationModel(newModel);	
		EnterpriseSimulationlModelUtilities.connectModelAndSimulation(newModel,simulation);
		return simulation;
	}

	
}
