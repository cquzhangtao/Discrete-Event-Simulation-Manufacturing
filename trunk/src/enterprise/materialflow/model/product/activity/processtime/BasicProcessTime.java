package enterprise.materialflow.model.product.activity.processtime;

import java.util.ArrayList;
import java.util.List;

import common.Entity;

import enterprise.materialflow.model.plant.resource.IResource;
import basic.volume.TimeVolume;

public class BasicProcessTime extends Entity implements IProcessTime{
	public static int num=0;
	private List<ProcessTimeElement> processTimeMap;

	public BasicProcessTime(){
		setName("ProcessTime"+num++);
		processTimeMap=new ArrayList<ProcessTimeElement> ();
	}
	public BasicProcessTime(boolean clone){
		super(clone);
	}
	
	public List<ProcessTimeElement> getProcessTimeMap() {
		return processTimeMap;
	}

	public void setProcessTimeMap(List<ProcessTimeElement> processTimeMap) {
		this.processTimeMap = processTimeMap;
	}

	@Override
	public long getProcessTime() {
		return 0;
	}

	@Override
	public long getProcessTime(IResource res) {
		return 0;
	}

	@Override
	public long getProcessTime(IResource res, int num) {
		return 0;
	}

	@Override
	public void addProcessTime(TimeVolume time) {
		addProcessTime(null,0,time);
	}

	@Override
	public void addProcessTime(IResource res, TimeVolume time) {		
		addProcessTime(res,0,time);
	}

	@Override
	public void addProcessTime(IResource res, int num, TimeVolume time) {
		ProcessTimeElement ele=new ProcessTimeElement();
		ele.setResource(res);
		ele.setTime(time);
		ele.setAmount(num);
		processTimeMap.add(ele);
	}

	@Override
	public IProcessTime clone() {
		return null;
	}
	
	public void clone(BasicProcessTime time){
		List<ProcessTimeElement> processingTimeMap=new ArrayList<ProcessTimeElement>();
		for(ProcessTimeElement element:this.processTimeMap){
			processingTimeMap.add(element.clone());
		}
		time.setProcessTimeMap(processingTimeMap);
		super.clone(time);
	}

	
}
