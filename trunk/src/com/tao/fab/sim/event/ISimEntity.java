package com.tao.fab.sim.event;

import simulation.core.ISimulation;

public interface ISimEntity {
	ISimulation getSimulation();
	void setSimulation(ISimulation sim);
}
