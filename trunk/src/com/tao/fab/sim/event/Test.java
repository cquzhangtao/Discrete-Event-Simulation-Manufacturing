package com.tao.fab.sim.event;

import simulation.core.ISimulation;
import simulation.core.Simulation;

public class Test {

	public static void main(String[] args) {
		
		ISimulation simulation=new Simulation();
		
		IRoute route=new Route();
		
		IStep step=new Step();
		step.setProcessTime(4);
		route.setFirstStep(step);
		
		IStep step1=new Step();
		step1.setProcessTime(3);
		step1.setNextStep(step);
		
		IStep step2=new Step();
		step2.setProcessTime(5);
		step2.setNextStep(step);
		
		
		IProduct product=new Product();
		product.setSimulation(simulation);
		IProductJob job=new ProductJob();
		job.setSimulation(simulation);
		job.setProduct(product);
		job.setRoute(route);
		
		product.setProductJob(job);
		
		EventHandler h = new EventHandler();
		h.releaseJob(product, 0);
		
		simulation.run();

	}

}
