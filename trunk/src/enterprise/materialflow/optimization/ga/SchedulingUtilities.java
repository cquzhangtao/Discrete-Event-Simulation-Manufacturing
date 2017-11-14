package enterprise.materialflow.optimization.ga;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import simulation.model.activity.IActivity;
import common.NetworkUtilities;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;

public class SchedulingUtilities {
	
	private SchedulingUtilities(){
		
	}

	public static void randomScheduling(List<ActivityEx> activities){
		Random rnd=new Random();
		randomAllocation(activities,rnd.nextInt());
		while(!randomSequencing(activities,rnd.nextInt())){
			System.out.println("Reallocated");
			randomAllocation(activities,rnd.nextInt());
			removeDisjunctiveArcs(activities);
		}
		removeDisjunctiveArcs(activities);
	}
	private static void randomAllocation(List<ActivityEx> activities,int seed){
		Random rnd = new Random(seed);
		for (ActivityEx activity : activities) {			
			List<IResource> usedRes=new ArrayList<IResource>();
			for(IActivity upAct1:NetworkUtilities.getUpstream(activity.getActivity())){				
				IProcessActivity upAct=(IProcessActivity) upAct1;
				if(upAct.getAssignedResource()!=null){
					usedRes.add(upAct.getAssignedResource());
				}
			}
			List<IResource> ress = activity.getActivity().getResourceRequirement().getAlternativeResources();
			List<IResource> possibleRes=new ArrayList<IResource>();
			for(IResource res:ress){
				if(!usedRes.contains(res)){
					possibleRes.add(res);
				}
			}
			if(possibleRes.size()>0){
				activity.getActivity().setAssignedResource(possibleRes.get(rnd.nextInt(possibleRes.size())));
			}else{
				activity.getActivity().setAssignedResource(ress.get(rnd.nextInt(ress.size())));
			}
		}
	}
	
	public static boolean randomSequencing(List<ActivityEx> activities,int seed){
		Random rnd = new Random(seed);
		Map<IResource, List<ActivityEx>> m = getActivitiyGroupedbyResource(activities);
		int position=0;
		for (List<ActivityEx> list : m.values()) {
			List<ActivityEx> sortedList = null;
			int num=0;
			while (true) {
				num++;
				sortedList = randomList(list, rnd.nextInt());
				boolean goodSequence = true;
				for (int i = 1; i < sortedList.size(); i++) {
					if (NetworkUtilities.inDownstream(sortedList.get(i).getActivity()
							,sortedList.get(i - 1).getActivity())&&NetworkUtilities.inDownstream(sortedList.get(i-1).getActivity()
							,sortedList.get(i).getActivity())) {
						goodSequence = false;
						break;
					}else if(NetworkUtilities.inDownstream(sortedList.get(i).getActivity()
							,sortedList.get(i - 1).getActivity())){
						ActivityEx preA = sortedList.get(i - 1);
						sortedList.remove(preA);
						sortedList.add(i, preA);
						i=i-2;
						if(i==-1){
							i=0;
						}
					}
				}
				if (goodSequence) {
					break;
				}
				if(num>1000){
					return false;
				}
			}
			sortedList.get(0).setPositionOnResource(position);		
//			for (int i = 1; i < sortedList.size(); i++) {
//				if(sortedList.get(i-1).addPredecessorFormCycle(sortedList.get(i))){
//					return false;
//				}
//				sortedList.get(i).setPositionOnResource(position+i);		
//			}
			for (int i = 1; i < sortedList.size(); i++) {
				if(sortedList.get(i).addAidPredecessor(sortedList.get(i - 1))){
					sortedList.get(i).setPositionOnResource(position+i);
				}else{
					//System.out.println("Something goes wrong,"+sortedList.get(i-1).getName()+","+sortedList.get(i).getName());
					return false;
				}		
			}
			position+=sortedList.size();
		}
		return true;
	
	}
	public static boolean addDisjunctiveArcs(List<ActivityEx> activities){
		Map<IResource, List<ActivityEx>> m = getActivitiyGroupedbyResource(activities);

		for (List<ActivityEx> list : m.values()) {
			ActivityEx[] sortedList=list.toArray(new ActivityEx[0]);
			Arrays.sort(sortedList);
			//sortedList[0].setPositionOnResource(0);
			for (int i = 1; i < sortedList.length; i++) {
				if(sortedList[i].addAidPredecessor(sortedList[i-1])){
					//sortedList[i].setPositionOnResource(i);
				}else{
					//System.out.println("Something goes wrong,"+sortedList[i-1].getName()+","+sortedList[i].getName());
					return false;
				}		
			}
		}
		return true;
	
	}
	public static void removeDisjunctiveArcs(List<ActivityEx> activities){
		for (ActivityEx activity : activities) {
			activity.getActivity().getSuccessors().removeAll(activity.getAidSuccessors());
			activity.getActivity().getPredecessors().removeAll(activity.getAidPredecessors());
			activity.getAidSuccessors().clear();
			activity.getAidPredecessors().clear();
		}		
	}
	public static Map<IResource, List<ActivityEx>> getActivitiyGroupedbyResource(
			List<ActivityEx> activities) {
		Map<IResource, List<ActivityEx>> m = new HashMap<IResource, List<ActivityEx>>();
		for (ActivityEx activity : activities) {
			List<ActivityEx> acts = m.get(activity.getActivity().getAssignedResource());
			if (acts == null) {
				acts = new ArrayList<ActivityEx>();
				m.put(activity.getActivity().getAssignedResource(), acts);
			}
			acts.add(activity);
		}
		return m;
	}


	private static List<ActivityEx> randomList(List<ActivityEx> list, int seed) {
		Random rnd = new Random(seed);
		List<ActivityEx> sortedList = new ArrayList<ActivityEx>();
		while (sortedList.size() < list.size()) {
			ActivityEx act = list.get(rnd.nextInt(list.size()));
			if (!sortedList.contains(act)&&containsAll(sortedList,getPredecessorsOnSameMachine(act.getActivity()))) {
				sortedList.add(act);
			}
		}
		return sortedList;
	}
	private static boolean containsAll(List<ActivityEx> list1,List<IActivity> list2){
		if(list2==null || list2.isEmpty()){
			return true;
		}
		List<IActivity> list3=new ArrayList<IActivity>();
		for(ActivityEx act:list1){
			list3.add(act.getActivity());
		}
		return list3.containsAll(list2);
		
	}
	private static List<IActivity> getPredecessorsOnSameMachine(IProcessActivity activity){
		List<IActivity> list=new ArrayList<IActivity>();
		for(IActivity act:activity.getPredecessors()){
			IProcessActivity pact=(IProcessActivity) act;
			if(pact.getAssignedResource()==activity.getAssignedResource()){
				list.add(act);
			}
		}
		return list;
		
	}
}
