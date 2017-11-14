package enterprise.materialflow.model.product.activity.processtime;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.plant.resource.IResource;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;

public class ResourceDependentProcessTime extends BasicProcessTime{

	private ResourceDependentProcessTime(boolean clone){
		super(clone);
	}
	public ResourceDependentProcessTime(){
		super();
	}
	
	public long getProcessTime(IResource res){
		for(ProcessTimeElement ele:getProcessTimeMap()){
			if(ele.getResource()==res){
				return ele.getTime().getMilliSeconds();
			}
		}
		return Long.MAX_VALUE;
	}
	
	@Override
	public long getProcessTime(IResource res, int num) {
		return getProcessTime(res);
	}
	
	public IProcessTime clone(){
		BasicProcessTime processTime=new ResourceDependentProcessTime(true);
		super.clone(processTime);
		return processTime;
		
	}
	
}
