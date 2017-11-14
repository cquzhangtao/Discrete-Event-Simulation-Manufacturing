package enterprise.materialflow.optimization.ga;
import java.util.ArrayList;
import java.util.List;

import simulation.model.activity.ActivityState;
import simulation.model.activity.IActivity;
import enterprise.materialflow.model.product.activity.IProcessActivity;


public class Network {
	
	public static List<IProcessActivity> getStartActivities(List<IProcessActivity> activities){
		List<IProcessActivity> starts=new ArrayList<IProcessActivity>();
		for(IProcessActivity act:activities){
			if(act.getPredecessors().size()==0){
				starts.add(act);
			}
		}
		
		return starts;
	}
	
	public static void forward(List<IProcessActivity> activities){
		for(IProcessActivity activity:activities){
			activity.setState(ActivityState.waiting);
		}
		List<IProcessActivity> starts = getStartActivities(activities);
		for(IProcessActivity act:starts){
			forward(act);
		}

	}
	
	public static void forward(IProcessActivity act1){
		if(act1.isStartable()){
			long max=0;
			for(IActivity act:act1.getPredecessors()){
				if(max<act.getEndTime()){
					max=act.getEndTime();
				}
			}
			long time=max;
			act1.setState(ActivityState.finished);
			act1.setStartTime(time);
			act1.setEndTime((long) (time+act1.getProcessTime().getProcessTime(act1.getAssignedResource())));
			for(IActivity sucact:act1.getSuccessors()){
				forward((IProcessActivity) sucact);
			}
		}
	}
	
	

}
