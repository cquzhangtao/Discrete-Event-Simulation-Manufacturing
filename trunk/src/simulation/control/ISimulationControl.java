package simulation.control;

import simulation.core.ISimulation;
import simulation.model.ISimulationModel;

public interface ISimulationControl {
	
	public void run();
	public void pause();
	public void goOn();
	public void reset();
	public void stop();
	public ISimulation getSimulation() ;

	public void setSimulation(ISimulation simulation) ;

	public ISimulationModel getModel() ;

	public void setModel(ISimulationModel model) ;

}
