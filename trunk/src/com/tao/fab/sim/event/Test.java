package com.tao.fab.sim.event;

public class Test {

	public static void main(String[] args) {
		
		Simulation simulation=new Simulation();
		IResourceGroup og=new ResourceGroup();
		og.setResourceNumber(1);
		
		IResourceGroup rg=new ResourceGroup();
		rg.setResourceNumber(1);
		rg.setJobTypeInFrontQueue(JobType.Lot);
		rg.setJobType(JobType.Lot);
		
		IJob prepaerJob=new Job();
		IRoute route=new Route();
		prepaerJob.setRoute(route);
		IStep step0=new Step();
		step0.setProcessTime(3);
		step0.setResourceGroup(og);
		route.setFirstStep(step0);
		rg.setPrepareJob(prepaerJob);
		//prepaerJob.setSimulation(simulation);
		

		
		
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
		//product.setSimulation(simulation);
		IProductJob job=new ProductJob();
		//job.setSimulation(simulation);
		job.setProduct(product);
		job.setRoute(route);
		//job.setType(JobType.Lot);
		
		product.setProductJob(job);
		
		EventHandler h = new EventHandler();
		//simulation.addEvents(h.releaseJob(product, 0));
		h.releaseJob(simulation.getEvnetLists(), product, 0);
		simulation.run();

	}

}
