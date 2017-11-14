package enterprise.materialflow.model.plant.resource;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.simulation.event.ResourceOfflineEvent;
import enterprise.materialflow.simulation.event.ResourceOnlineEvent;
import simulation.core.event.ISimulationEvent;
import simulation.model.activity.IActivity;
import basic.volume.TimeVolume;

public class InterruptableResource extends SetupableResource implements
		IInterruptableResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1931301664688710724L;
	private TimeVolume offlineTime;
	private TimeVolume timeToNextOffline;
	private boolean offlineEventSuspended;

	@Override
	public List<ISimulationEvent> offline(long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (getState() == ResourceState.idle) {
			ResourceOnlineEvent event = new ResourceOnlineEvent(this);
			events.add(event);
			event.setTime(time + offlineTime.getMilliSeconds());
			setState(ResourceState.offline);

		} else {
			offlineEventSuspended = true;
		}
		return events;
	}

	@Override
	public List<ISimulationEvent> online(long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		setState(ResourceState.idle);
		events.addAll(start(time));
		ResourceOfflineEvent event = new ResourceOfflineEvent(this);
		events.add(event);
		event.setTime(time + timeToNextOffline.getMilliSeconds());

		return events;
	}

	@Override
	public List<ISimulationEvent> free(IActivity activity, long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (offlineEventSuspended) {
			offlineEventSuspended = false;
			ResourceOnlineEvent event = new ResourceOnlineEvent(this);
			events.add(event);
			event.setTime(time + offlineTime.getMilliSeconds());
			setState(ResourceState.offline);
		} else {
			
			events.addAll(super.free(activity, time));
		
		}
		return events;

	}

	@Override
	public List<ISimulationEvent> queue(IActivity activity, long time) {
		ArrayList<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		if (getState() == ResourceState.idle && offlineEventSuspended) {
			getQueue().add(activity);
			offlineEventSuspended = false;
			ResourceOnlineEvent event = new ResourceOnlineEvent(this);
			events.add(event);
			event.setTime(time + offlineTime.getMilliSeconds());
			setState(ResourceState.offline);
		} else {	
			events.addAll(super.queue(activity, time));
		}
		return events;

	}

	public TimeVolume getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(TimeVolume offlineTime) {
		this.offlineTime = offlineTime;
	}

	public TimeVolume getTimeToNextOffline() {
		return timeToNextOffline;
	}

	public void setTimeToNextOffline(TimeVolume timeToNextOffline) {
		this.timeToNextOffline = timeToNextOffline;
	}
	
	public IResource clone(){
		IInterruptableResource resource=new InterruptableResource();
		resource.setOfflineTime(offlineTime);
		resource.setTimeToNextOffline(timeToNextOffline);
		resource.setOfflineEventSuspended(offlineEventSuspended);
		super.clone(resource);
		return resource;
		
	}

	public boolean isOfflineEventSuspended() {
		return offlineEventSuspended;
	}

	public void setOfflineEventSuspended(boolean offlineEventSuspended) {
		this.offlineEventSuspended = offlineEventSuspended;
	}

}
