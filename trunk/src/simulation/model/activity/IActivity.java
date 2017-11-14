package simulation.model.activity;
import java.util.List;

import common.IEntity;
import common.INetworkEntity;
import common.ITimeInterval;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import simulation.core.event.ISimulationEvent;


public interface IActivity extends IEntity,ITimeInterval,INetworkEntity<IActivity>{
	public List<ISimulationEvent> end(long time);
	public List<ISimulationEvent> start(long time);
	public boolean isFinished();
	public boolean isStartable();
	public boolean isPredecessor(IActivity activity);
	public boolean isSuccessor(IActivity activity);
	public boolean addPredecessor(IActivity predecessor);
	public void addActivityListener(ActivityListener listener);
	public IActivity clone();
	public List<ActivityListener> getListeners();
	public void setState(ActivityState finished);
	
	



}
