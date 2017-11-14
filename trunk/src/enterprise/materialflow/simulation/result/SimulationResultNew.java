package enterprise.materialflow.simulation.result;

import common.Entity;
import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import enterprise.materialflow.simulation.result.data.ProductDataset;
import basic.volume.TimeVolume;

public class SimulationResultNew extends Entity {
	private static ProductDataset productDataset = new ProductDataset();
	private static ProductDataset iproductDataset = new ProductDataset();
	private static boolean subSimulation = false;

	public static double getKPI(KeyPerformanceEnmu kpi) {
		if (subSimulation) {
			return getKPI(kpi, iproductDataset);
		} else {
			return getKPI(kpi, productDataset);
		}
	}

	private static double getKPI(KeyPerformanceEnmu kpi, ProductDataset dataset) {
		if (kpi == KeyPerformanceEnmu.averageCT) {
			return dataset.getSummaryProductDataset().getAvgCycleTime();
		} else if (kpi == KeyPerformanceEnmu.averageWIP) {
			return dataset.getSummaryProductDataset().getAvgWip();
		}else if(kpi==KeyPerformanceEnmu.makespan){
			return dataset.getSummaryProductDataset().getMakespan();
		}
		return 0;
	}

	public static void save() {
		subSimulation = true;
	}

	public static void reset() {
		iproductDataset.reset();
	}

	public static void recover() {
		subSimulation = false;
	}

	public static ProductDataset getProductDataset() {
		if (subSimulation) {
			return iproductDataset;
		} else {
			return productDataset;
		}
	}

	public static void addProductData(IProduct product, long time) {
		if (subSimulation) {
			iproductDataset.addProductData(product.getName(), time);
		} else {
			productDataset.addProductData(product.getName(), time);
		}
	}

	public static void addProductData(IProduct product, long time,
			double cycletime) {
		if (subSimulation) {
			iproductDataset.addProductData(product.getName(), time, cycletime);
		} else {
			productDataset.addProductData(product.getName(), time, cycletime);
		}
	}

	public static void init(IEnterpriseSimulationModel model, TimeVolume timeVolume) {
		iproductDataset.init(model.getProducts(),
				timeVolume.getMilliSeconds());
		productDataset.init(model.getProducts(),
				timeVolume.getMilliSeconds());

	}

	public static void stat() {
		if (subSimulation) {
			iproductDataset.stat();
			;
		} else {
			productDataset.stat();
		}

	}

	public static String getProductResultString() {
		if (subSimulation) {
			return iproductDataset.getString();
		} else {
			return productDataset.getString();
		}
	}

}
