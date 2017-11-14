package enterprise.materialflow.model.plan;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.release.BasicJobRelease;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import enterprise.materialflow.model.plan.order.InfiniteManufactureOrder;
import enterprise.materialflow.model.plan.order.ManufactureOrder;
import enterprise.materialflow.model.product.IProduct;
import basic.distribution.Distribution;
import basic.distribution.DistributionParameterEnum;
import basic.distribution.TimePoint;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class ProductionPlanUtilities {
	public static List<IManufactureOrder> generatorBOMOrders(IProductionPlan plan) {
		ArrayList<IManufactureOrder> bomOrders = new ArrayList<IManufactureOrder>();
		bomOrders.addAll(plan.getOrders());
		for (IManufactureOrder order : plan.getOrders()) {
			bomOrders.addAll(getDependenceOrders(order));
		}
		return bomOrders;
	}

	private static List<IManufactureOrder> getDependenceOrders(IManufactureOrder order) {
		List<IManufactureOrder> subOrders = new ArrayList<IManufactureOrder>();
		getSubOrders(order, order.getProduct(), subOrders);
		return subOrders;
	}

	private static void getSubOrders(IManufactureOrder parent, IProduct product,
			List<IManufactureOrder> subOrders) {
		for (IProduct prod : product.getDependenceProducts().keySet()) {
			IManufactureOrder order=null;
			if(parent instanceof InfiniteManufactureOrder){
				order = new InfiniteManufactureOrder(prod);
			}else{
				order = new ManufactureOrder(prod);
				order.setAmount(product.getDependenceProducts().get(prod)
						* parent.getAmount());
			}
			order.setParent(parent);
//			IJobRelease jobRelease = new BasicJobRelease(order);
//			((BasicJobRelease) jobRelease).setInterval(new TimeVolume(50,
//					TimeUnitEnum.Hour));
//			order.setJobRelease(jobRelease);
			TimePoint time = new TimePoint((double) (parent.getReleaseTime()));
			order.setReleaseTime(time);
			subOrders.add(order);
			getSubOrders(order, prod, subOrders);
		}
	}
}
