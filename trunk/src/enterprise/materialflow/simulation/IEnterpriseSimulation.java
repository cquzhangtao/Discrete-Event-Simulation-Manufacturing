package enterprise.materialflow.simulation;

import java.util.List;

import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.core.ISimulation;
import simulation.core.others.ISimulationTerminateCondition;
import simulation.model.activity.IActivity;

public interface IEnterpriseSimulation extends ISimulation{
	public IEnterpriseSimulationModel getEnterpriseSimulationModel();
	public void setEnterpriseSimulationModel(IEnterpriseSimulationModel enterprise);
	public IEnterpriseSimulation clone();	
}
