package enterprise.materialflow.model.product.activity.processtime;

import enterprise.materialflow.model.plant.resource.IResource;



public class ResourceIndependentProcessTime extends BasicProcessTime{
	
	
	public IProcessTime clone(){
		ResourceIndependentProcessTime processTime=new ResourceIndependentProcessTime();
		super.clone(processTime);
		return processTime;
		
	}
	public long getProcessTime() {
		return getProcessTimeMap().get(0).getTime().getMilliSeconds();
	}
	
	@Override
	public long getProcessTime(IResource res, int num) {
		return getProcessTime();
	}
	
}
