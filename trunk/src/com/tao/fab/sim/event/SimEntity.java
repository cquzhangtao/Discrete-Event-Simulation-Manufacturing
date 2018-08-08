package com.tao.fab.sim.event;

import simulation.core.ISimulation;

public class SimEntity implements ISimEntity{

	private ISimulation simulation;
	@Override
	public ISimulation getSimulation() {

		return simulation;
	}
	@Override
	public void setSimulation(ISimulation sim) {
		simulation=sim;
		
	}

}
