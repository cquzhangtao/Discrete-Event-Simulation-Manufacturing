package com.tao.fab.sim.event;

public interface IProductJob extends IJob{

	IProduct getProduct();

	static IProductJob newInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	void setProduct(IProduct product);
	
	IProductJob clone();

}
