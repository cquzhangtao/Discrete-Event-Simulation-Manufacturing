package enterprise.materialflow.model.product.activity.processtime;


import enterprise.materialflow.model.plant.resource.IResource;

public class ResourceAmountDPDependentProcessTime extends BasicProcessTime {
	public long getProcessTime(IResource res,int amount){
		for(ProcessTimeElement ele:getProcessTimeMap()){
			if(ele.getResource()==res){
				return ele.getTime().getMilliSeconds()*amount/ele.getAmount();
			}
		}
		return Long.MAX_VALUE;
	}
	
	public IProcessTime clone(){
		BasicProcessTime processTime=new ResourceAmountDPDependentProcessTime();
		super.clone(processTime);
		return processTime;
		
	}
	
	
}
