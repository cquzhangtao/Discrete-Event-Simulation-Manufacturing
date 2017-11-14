package enterprise.materialflow.simulation.result.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enterprise.materialflow.model.product.IProduct;


public class ProductDataset implements Serializable {

	private Map<String,ProductData> productDataset = new HashMap<String,ProductData>();
	private  ProductData summaryProductDataset = new ProductData("All");
	

	public void init(Map<String, IProduct> products, long interval) {
		summaryProductDataset.setInterval(interval);
		for (IProduct product:products.values()) {
			ProductData rd = new ProductData(product.getName());
			rd.setRawProcessTime(product.getRawProcessingTime());
			rd.setInterval(interval);
			productDataset.put(product.getName(),rd);
		}
	}

	public void save() {
		for (ProductData r : productDataset.values())
			r.save();
		summaryProductDataset.save();
	}

	public void recover() {
		for (ProductData r : productDataset.values())
			r.recover();
		summaryProductDataset.recover();
	}

	private void addWIP(String index, long time, int iwip, int allWip) {
		productDataset.get(index).addWIP(time, iwip);
		summaryProductDataset.addWIP(time, allWip);
	}

	private void addCycleTime(String index, long time, double cycleTime) {
		productDataset.get(index).addCycleTime(time, cycleTime);
		summaryProductDataset.addCycleTime(time, cycleTime);
	}

	private void addReleaseTime(String index, long time) {
		productDataset.get(index).addReleaseTime(time);
		summaryProductDataset.addReleaseTime(time);
	}
	
	public void addProductData(String product,long time){
		addReleaseTime(product,time);
		addWIP(product,time,productDataset.get(product).getLastWip()+1,summaryProductDataset.getLastWip()+1);
	}
	public void addProductData(String product,long time, double cycleTime){
		addCycleTime(product,time,cycleTime);
		addWIP(product,time,productDataset.get(product).getLastWip()-1,summaryProductDataset.getLastWip()-1);
	}

	public  void stat() {
		for (ProductData data:productDataset.values()) {
			data.stat();
		}
		summaryProductDataset.stat();
	}

	public void reset() {
		for (ProductData data:productDataset.values()) {
			data.reset();
		}
		summaryProductDataset.reset();
	}
	
	public String getString() {
		String str=summaryProductDataset.toString();
		for (ProductData data:productDataset.values()) {
			String s=data.toString();
			if(s.isEmpty()){
				continue;
			}
			str+="\n"+data.toString();
		}
		return str;
	}

	public ProductData getSummaryProductDataset() {
		return summaryProductDataset;
	}

	public void setAllReleaseDataset(ProductData allReleaseDataset) {
		this.summaryProductDataset = allReleaseDataset;
	}
}
