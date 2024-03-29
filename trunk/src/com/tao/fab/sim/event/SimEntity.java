package com.tao.fab.sim.event;

import common.Entity;
import simulation.core.ISimulation;

public class SimEntity extends Entity implements ISimEntity{

	private ISimulation simulation;
	@Override
	public ISimulation getSimulation() {

		return simulation;
	}
	@Override
	public void setSimulation(ISimulation sim) {
		simulation=sim;
		
	}
	
	public  ISimEntity clone(){
		 ISimEntity entity=new SimEntity();
		entity.setSimulation(simulation);
		return entity;
	}
	
	public void clone(ISimEntity enity){
		enity.setSimulation(simulation);
	}

}
