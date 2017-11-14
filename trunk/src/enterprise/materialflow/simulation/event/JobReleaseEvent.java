package enterprise.materialflow.simulation.event;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.routing.STTDResourceSelector;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.JobUtilities;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.ProcessActivity;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import enterprise.materialflow.simulation.result.data.ProductDataset;
import simulation.core.event.BasicSimulationEvent;
import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;
import simulation.model.activity.IActivity;

public class JobReleaseEvent extends BasicSimulationEvent {
	private IJobRelease jobRelease;

	public JobReleaseEvent(IJobRelease release) {
		this.jobRelease = release;
		setType(SimulationEventType.normal);
	}

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		List<ISimulationEvent> events = new ArrayList<ISimulationEvent>();
		List<IJob> jobs = jobRelease.releaseJobs(currentTime);
		for (IJob job : jobs) {
			for (IActivity activity : JobUtilities.getStartActivities(job)) {
				if (activity.isStartable()) {
					ProcessActivity pactivity = (ProcessActivity) activity;
					if (pactivity.getResourceSelector() instanceof STTDResourceSelector) {
						ActivitySelectingResourceEvent event = new ActivitySelectingResourceEvent();
						event.setActivity(pactivity);
						event.setTime(currentTime);
						events.add(event);
					} else {
						
						IResource res = pactivity.getResourceSelector()
								.selectResource(pactivity,
										pactivity.getAlternativeResources());
						pactivity.setAssignedResource(res);
						events.addAll(res.queue(pactivity, currentTime));
					}
				}
			}
		}
		if (jobRelease.isDone()) {
			return events;
		}
		JobReleaseEvent event = new JobReleaseEvent(jobRelease);
		event.setTime(jobRelease.nextReleaseTime(currentTime));
		events.add(event);
		return events;
	}

	public IJobRelease getJobRelease() {
		return jobRelease;
	}

	public void setJobRelease(IJobRelease jobRelease) {
		this.jobRelease = jobRelease;
	}

	public JobReleaseEvent clone() {
		JobReleaseEvent event = new JobReleaseEvent(jobRelease);
		super.clone(event);
		return event;
	}

}
