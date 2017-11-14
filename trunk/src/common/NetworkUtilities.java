package common;

import java.util.ArrayList;
import java.util.List;



public class NetworkUtilities {
	public static <T extends INetworkEntity<T>> boolean inDownstream(T act1,T act2){
		if(act1.getSuccessors().size()==0){
			return false;
		}
		for(T act:act1.getSuccessors()){
			if(act==act2){
				return true;
			}
			if( inDownstream(act,act2)){
				return true;
			}
		}
		
		return false;
		
	}

	
	public static <T extends INetworkEntity<T>> boolean inUpstream(T act1,T act2){
		if(act1.getPredecessors().size()==0){
			return false;
		}
		for(T act:act1.getPredecessors()){
			if(act==act2){
				return true;
			}
			if( inUpstream(act,act2)){
				return true;
			}
		}
		
		return false;
		
	}
	
	public static <T extends INetworkEntity<T>> boolean addPredecessor(T activity,T predecessor){
		if(activity!=predecessor&&!inDownstream(activity,predecessor)&&!inUpstream(predecessor,activity)){
			if(!activity.getPredecessors().contains(predecessor)){
				activity.getPredecessors().add(predecessor);
			}
			if(!predecessor.getSuccessors().contains(activity)){
				predecessor.getSuccessors().add(activity);
			}
			return true;
		}
		return false;
	}
	
	public static <T extends INetworkEntity<T>> boolean addSuccessor(T activity,T successor){
		if(activity!=successor&&!inDownstream(successor,activity)&&!inUpstream(activity,successor)){
			if(!activity.getSuccessors().contains(successor)){
				activity.getSuccessors().add(successor);
			}
			if(!successor.getPredecessors().contains(activity)){
				successor.getPredecessors().add(activity);
			}
			return true;
		}
		return false;
	}
	
	public static <T extends INetworkEntity<T>> List<T> getUpstream(T activity){
		List<T> list=new ArrayList<T>();
		getUpstream(activity,list);
		
		return list;
	}
	
	private static <T extends INetworkEntity<T>> void getUpstream(T activity,List<T> list){
		if(activity.getPredecessors().size()==0){
			return;
		}
		for(T act:activity.getPredecessors()){
			list.add(act);
			getUpstream(act,list);
		}
		
	}
	public static <T extends INetworkEntity<T>> List<T> getDownstream(T activity){
		List<T> list=new ArrayList<T>();
		getDownstream(activity,list);
		
		return list;
	}
	
	private static <T extends INetworkEntity<T>> void getDownstream(T activity,List<T> list){
		if(activity.getSuccessors().size()==0){
			return;
		}
		for(T act:activity.getSuccessors()){
			list.add(act);
			getDownstream(act,list);
		}
		
	}
	


}
