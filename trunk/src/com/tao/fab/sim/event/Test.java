package com.tao.fab.sim.event;

import simulation.core.ISimulation;
import simulation.core.Simulation;

public class Test {

	public static void main(String[] args) {
		
		ISimulation simulation=new Simulation();
		
		IResourceGroup rg=new ResourceGroup();
		rg.setResourceNumber(1);
		rg.setJobTypeInFrontQueue(JobType.Lot);
		rg.setJobType(JobType.Lot);
		
		IJob prepaerJob=new Job();
		IRoute route=new Route();
		prepaerJob.setRoute(route);
		IStep step0=new Step();
		step0.setProcessTime(3);
		route.setFirstStep(step0);
		
		
		
		 route=new Route();
		
		IStep step=new Step();
		step.setProcessTime(4);
		route.setFirstStep(step);
		
		step.setResourceGroup(rg);
		
		IStep step1=new Step();
		step1.setProcessTime(3);
		step.setNextStep(step1);
		
		IStep step2=new Step();
		step2.setProcessTime(5);
		step1.setNextStep(step2);
		
		
		IProduct product=new Product();
		product.setSimulation(simulation);
		IProductJob job=new ProductJob();
		job.setSimulation(simulation);
		job.setProduct(product);
		job.setRoute(route);
		job.setType(JobType.Lot);
		
		product.setProductJob(job);
		
		EventHandler h = new EventHandler();
		simulation.addEvents(h.releaseJob(product, 0));
		
		simulation.run();

	}

}
