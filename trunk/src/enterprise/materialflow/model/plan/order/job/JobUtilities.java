package enterprise.materialflow.model.plan.order.job;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;
import simulation.model.event.ActivityRelatedEvent;

public class JobUtilities {

	public static List<IActivity> getStartActivities(IJob job) {
		List<IActivity> starts = new ArrayList<IActivity>();
		for (IActivity act : job.getActivities().values()) {
			if (act.getPredecessors().isEmpty()) {
				starts.add(act);
			}
		}
		return starts;
	}

	public static List<IProcessActivity> getEndActivities(IJob job) {
		List<IProcessActivity> ends = new ArrayList<IProcessActivity>();
		for (IProcessActivity act : job.getActivities().values()) {
			if (act.getSuccessors().isEmpty()) {
				ends.add(act);
			}
		}
		return ends;
	}

	public static Map<String, IProcessActivity> generateActivities(IJob job,IProduct product) {
		Map<String,IProcessActivity> activities = new Hashtable<String,IProcessActivity>();
		for (IProcessActivity pact : product.getActivities().values()) {
	
			IProcessActivity newact =pact.clone();
			newact.setJob(job);
			newact.setName(job.getName() + "_" + newact.getName());
			activities.put(newact.getName(),newact);
		}
		for (IActivity activity : activities.values()) {
			List<IActivity> precedesors = new ArrayList<IActivity>();
			List<IActivity> successors = new ArrayList<IActivity>();
			for (IActivity pre : activity.getPredecessors()) {
				precedesors.add(activities.get(job.getName() + "_" +pre.getName()));
			}
			for (IActivity suc : activity.getSuccessors()) {
				successors.add(activities.get(job.getName() + "_" +suc.getName()));
			}
			activity.setSuccessors(successors);
			activity.setPredecessors(precedesors);
		}
		return activities;

	}
	
}
