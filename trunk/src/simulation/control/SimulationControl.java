package simulation.control;

import simulation.core.ISimulation;
import simulation.core.others.ISimulationTerminateCondition;
import simulation.model.ISimulationModel;

public class SimulationControl implements ISimulationControl{
	
	private ISimulationModel model;
	private ISimulation simulation;
	
	
	public SimulationControl(ISimulationModel model,ISimulation simulation){
		this.model=model;
		this.simulation=simulation;		
		simulation.addEvents(model.getInitialEvents());
	}

	@Override
	public void run() {
		simulation.run();
		
	}

	@Override
	public void pause() {
		simulation.stop();	
		
	}

	@Override
	public void goOn() {
		simulation.run();
		
	}

	@Override
	public void reset() {
		model.reset();
		simulation.getEventList().clear();
		simulation.setCurrentTime(0);
		simulation.addEvents(model.getInitialEvents());
	}

	@Override
	public void stop() {
		simulation.stop();		
	}

	public ISimulation getSimulation() {
		return simulation;
	}

	public void setSimulation(ISimulation simulation) {
		this.simulation = simulation;
	}

	public ISimulationModel getModel() {
		return model;
	}

	public void setModel(ISimulationModel model) {
		this.model = model;
	}

}
