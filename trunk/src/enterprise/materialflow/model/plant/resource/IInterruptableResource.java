package enterprise.materialflow.model.plant.resource;

import java.util.List;

import simulation.core.event.ISimulationEvent;
import basic.volume.TimeVolume;

public interface IInterruptableResource extends ISetupableResource{
	public List<ISimulationEvent> offline(long time);
	public List<ISimulationEvent> online(long time);
	public TimeVolume getOfflineTime();
	public void setOfflineTime(TimeVolume offlineTime);
	public TimeVolume getTimeToNextOffline();
	public void setTimeToNextOffline(TimeVolume timeToNextOffline);
	public void setOfflineEventSuspended(boolean offlineEventSuspended);

}
