package com.tao.fab.sim.event;

public class ProductJob extends Job implements IProductJob{

	private IProduct product;
	public ProductJob() {
		
		
	}

	@Override
	public IProduct getProduct() {
		
		return product;
	}

	@Override
	public void setProduct(IProduct product) {
		this.product=product;
		
	}

	@Override
	public IProductJob clone() {
		IProductJob job=new ProductJob();
		job.setProduct(product);
		job.setRoute(getRoute());
		return job;
	}

	

}
