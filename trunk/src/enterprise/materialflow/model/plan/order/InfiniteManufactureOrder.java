package enterprise.materialflow.model.plan.order;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.product.IProduct;
import basic.distribution.Distribution;
import basic.distribution.DistributionParameterEnum;
import basic.distribution.TimePoint;

public class InfiniteManufactureOrder extends ManufactureOrder{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3752157661235472186L;
	private InfiniteManufactureOrder(){
		super();
		super.setAmount(Integer.MAX_VALUE);		
	}
	
	public InfiniteManufactureOrder(IProduct product){
		super(product);
		super.setAmount(Integer.MAX_VALUE);		
	}
	@Override
	public void setAmount(int amount){
		//do nothing
	}
	
	public IManufactureOrder clone(){
		IManufactureOrder order=new InfiniteManufactureOrder();
		super.clone(order);
		return order;
	}
}
