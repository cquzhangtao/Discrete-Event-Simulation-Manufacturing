package enterprise.materialflow.model.product.activity.processtime;

import java.util.ArrayList;
import java.util.List;

import common.IEntity;

import enterprise.materialflow.model.plant.resource.IResource;
import basic.volume.TimeVolume;

public interface IProcessTime extends IEntity{
	
	public long getProcessTime();
	public long getProcessTime(IResource res);
	public long getProcessTime(IResource res,int num);
	
	public void addProcessTime(TimeVolume time);
	public void addProcessTime(IResource res,TimeVolume time);
	public void addProcessTime(IResource res,int num,TimeVolume time);
	
	public void setProcessTimeMap(List<ProcessTimeElement> arrayList);
	public List<ProcessTimeElement>  getProcessTimeMap();
	public IProcessTime clone();

}
