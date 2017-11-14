package enterprise.materialflow.model.product.activity.processtime;


import enterprise.materialflow.model.plant.resource.IResource;

public class ResourceAmountDependentProcessTime extends BasicProcessTime{
	public long getProcessTime(IResource res,int amount){
		for(ProcessTimeElement ele:getProcessTimeMap()){
			if(ele.getResource()==res&&ele.getAmount()==amount){
				return ele.getTime().getMilliSeconds();
			}
		}
		return Long.MAX_VALUE;
	}
	
	public IProcessTime clone(){
		BasicProcessTime processTime=new ResourceAmountDependentProcessTime();
		super.clone(processTime);
		return processTime;
		
	}
}
