package com.tao.fab.sim.event;

public interface IProductJob extends IJob{

	IProduct getProduct();


	void setProduct(IProduct product);
	
	IProductJob clone();


	


	

}
