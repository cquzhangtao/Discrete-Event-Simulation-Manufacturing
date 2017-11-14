package ui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Entity;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;


public class GraphicNetworkData extends Entity{
	private HashMapR<IGraphicActivity,IActivity> activities=new HashMapR<IGraphicActivity,IActivity>();;
	public GraphicNetworkData(List<IProcessActivity> acts){
		
		for(IActivity activity:acts){
			activities.put(new GraphicActivity(activity),activity);
		}
		
		for(IGraphicActivity gact:activities.keySet()){
			for(IActivity activity:activities.get(gact).getPredecessors()){
				gact.getPredecessors().add(activities.getR(activity));
			}
			for(IActivity activity:activities.get(gact).getSuccessors()){
				gact.getSuccessors().add(activities.getR(activity));
			}			
		}
		
	}	
	public List<IGraphicActivity> getActivities() {
		return new ArrayList<IGraphicActivity>(activities.keySet());
	}


	
	public List<IGraphicActivity> getStartActivities(){
		List<IGraphicActivity> starts=new ArrayList<IGraphicActivity>();
		for(IGraphicActivity act:activities.keySet()){
			if(act.getPredecessors().size()==0){
				starts.add(act);
			}
		}
		
		return starts;
	}
	public List<IGraphicActivity> getEndActivities(){
		List<IGraphicActivity> starts=new ArrayList<IGraphicActivity>();
		for(IGraphicActivity act:activities.keySet()){
			if(act.getSuccessors().size()==0){
				starts.add(act);
			}
		}
		
		return starts;
	}
	
	public void generatorCol(){
		List<IGraphicActivity> starts = getStartActivities();
		for(IGraphicActivity act:starts){
			generatorCol(act);
		}
		for(IGraphicActivity activity:activities.keySet()){
			activity.setState(GraphicActivityState.unfinished);
		}
	}
	
	public void generatorCol(IGraphicActivity act){
		int col=act.startable();
		if(col!=-1){
			act.setState(GraphicActivityState.finished);
			act.setCol(col+1);
			for(IGraphicActivity sucact:act.getSuccessors()){
				generatorCol((IGraphicActivity) sucact);
			}
		}
	}
	public void generateRow(){
		Map<Integer,List<IGraphicActivity>> m=new HashMap<Integer,List<IGraphicActivity>>();
		for(IGraphicActivity activity:activities.keySet()){
			List<IGraphicActivity>acts=m.get(activity.getCol());
			if(acts==null){
				acts=new ArrayList<IGraphicActivity>();
				m.put(activity.getCol(), acts);
			}
			acts.add(activity);
		}
		int flag=0;
		for(List<IGraphicActivity>list:m.values()){
			
			for(int i=0;i<list.size();i++){
				list.get(i).setRow(i*2+flag);
			}
			if(flag==0){
				flag=1;
			}else{
				flag=0;
			}
		}
	}
	
	public void setPosition(){
		setLevelForNodes();
		ArrayList<Integer> numList=new ArrayList<Integer>();
		int max=Integer.MIN_VALUE;
		int maxcol=0;
		for(IGraphicActivity act:getEndActivities()){
			if(maxcol<act.getCol()){
				maxcol=act.getCol();
			}
		}

		for(int i=0;i<=maxcol;i++){
			int sum=0;
			for (IGraphicActivity node : activities.keySet()) {
				if (node.getCol() == i) {
					sum++;
				}
			}
			numList.add(sum);
			if(max<sum){
				max=sum;
			}
			
		}
		int index=1;
		for (IGraphicActivity node : getStartActivities()) {
			
			node.setRow((int) (1.0*max/getStartActivities().size()*80)*index);
			index++;
		}
		for(int i=1;i<=maxcol;i++){
			for (IGraphicActivity node : activities.keySet()) {
				
				if (node.getCol() == i) {
					setPosition(node,1.0*max/numList.get(i));
				}
			}
		}
		
		
	}
	

	
	public void setPosition(IGraphicActivity node,double yP){
		if(node.getPredecessors().size()==1){
			double index=node.getPredecessors().get(0).getSuccessors().indexOf(node.getPredecessors().get(0));
			double num=node.getPredecessors().get(0).getSuccessors().size();
			node.setRow((int) (node.getPredecessors().get(0).getRow()+(index-(num-1)/2)*yP*30));
			
		}else{
			double sum=0;
			for(IGraphicActivity side:node.getPredecessors()){
				sum+=side.getRow();
			}
			node.setRow((int) (sum/node.getPredecessors().size()));
		}
	}
	
	public void setLevelForNodes() {

		HashMap<String, Integer> preActExeSituation = new HashMap<String, Integer>();
		HashMap<String, Double> activityStep = new HashMap<String, Double>();
		for (IGraphicActivity node : activities.keySet()) {
			preActExeSituation.put(node.getName(), 0);
		}
		for (IGraphicActivity node : getStartActivities()) {
			
			activityStep.put(node.getName(), 0d);
		}
		for (IGraphicActivity node : getStartActivities()) {
			setLevelForNodes(node, preActExeSituation, activityStep);
		}

	}


	public void setLevelForNodes(IGraphicActivity node, Map<String, Integer> preActExeSituation, Map<String, Double> activityStep) {
		// one predecessor of the node is executed
		if (node.getPredecessors().size() != 0) {
			preActExeSituation.put(node.getName(),
					preActExeSituation.get(node.getName()) + 1);
		}

		// all predecessor are executed
		if (node.getPredecessors().size() == preActExeSituation.get(node.getName())
				.intValue()) {
			// initial node
			if (node.getPredecessors().size() == 0) {
				
//			} else if (node.getInputLogic() == NodeLogic.ALTERNATIVE) {
//				double sum=0;
//				for (Side activity : node.getIn()) {
//					sum+= activityStep.get(activity.getId()).doubleValue();
//				}
//				node.setLevel(sum/node.getIn().size());

			} else  {
				double maxStep = Double.MIN_VALUE;
				for (IGraphicActivity activity : node.getPredecessors()) {
					if (maxStep < activityStep.get(activity.getName()).doubleValue()) {
						maxStep = activityStep.get(activity.getName()).doubleValue();
					}

				}
				node.setCol((int) maxStep+1);
			}
			if (node.getSuccessors().size() == 0) {
				// System.out.println("time: " + beginingValue);
				return;
			}
			// go ahead
			for (IGraphicActivity activity : node.getSuccessors()) {
				activityStep.put(activity.getName(), (double) (node.getCol() + 1));
				setLevelForNodes(activity,preActExeSituation,activityStep);
			}
		}

	}

}
