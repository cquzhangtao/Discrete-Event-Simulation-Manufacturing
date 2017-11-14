package enterprise.materialflow.model.plant.resource;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.plant.resource.setup.ISetupTime;
import enterprise.materialflow.model.plant.resource.setup.NonSetupTime;
import enterprise.materialflow.simulation.event.ResourceSetupEndEvent;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;

public class SetupableResource extends Resource implements ISetupableResource{
	
	private ISetupTime setupTime;
	private IActivity waitingSetupActivity;
	private IActivity preActivity;
	
	public SetupableResource(){
		setupTime=new NonSetupTime();
	}
	
	
	public ISetupTime getSetupTime() {
		return setupTime;
	}
	public void setSetupTime(ISetupTime setupTime) {
		this.setupTime = setupTime;
	}
	@Override
	public List<ISimulationEvent> free(IActivity activity,long time) {
		preActivity=activity;
		return super.free(activity, time);
		
	}
	@Override
	public List<ISimulationEvent> start(IActivity act,long time){
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if(setupTime.getTime(preActivity,act )>0){
			setState(ResourceState.setup);
			waitingSetupActivity=act;
			ResourceSetupEndEvent event=new ResourceSetupEndEvent(this);
			event.setTime(time+setupTime.getTime(preActivity, act));
			events.add(event);
			
		}else{
			events.addAll(super.start(act, time));
		}
		return events;
	}

	@Override
	public List<ISimulationEvent> endSetup(long currentTime) {
		setState(ResourceState.idle);	
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		events.addAll(super.start(waitingSetupActivity,currentTime));
		return events;
	}
	
	public void clone(ISetupableResource resource){
		resource.setSetupTime(setupTime);
		resource.setPreActivity(preActivity);
		resource.setWaitingSetupActivity(waitingSetupActivity);
		super.clone(resource);
	}


	public IActivity getWaitingSetupActivity() {
		return waitingSetupActivity;
	}


	public void setWaitingSetupActivity(IActivity waitingSetupActivity) {
		this.waitingSetupActivity = waitingSetupActivity;
	}


	public IActivity getPreActivity() {
		return preActivity;
	}


	public void setPreActivity(IActivity preActivity) {
		this.preActivity = preActivity;
	}
}
