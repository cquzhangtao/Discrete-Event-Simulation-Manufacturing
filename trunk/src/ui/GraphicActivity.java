package ui;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.model.product.activity.IProcessActivity;
import simulation.model.activity.IActivity;

public class GraphicActivity  implements IGraphicActivity{
	private IActivity activity;
	private GraphicActivityState state=GraphicActivityState.unfinished;
	private List<IGraphicActivity> predecessors=new ArrayList<IGraphicActivity>();
	private List<IGraphicActivity> successors=new ArrayList<IGraphicActivity>();;
	public IActivity getActivity() {
		return activity;
	}
	public void setActivity(IActivity activity) {
		this.activity = activity;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	private int col,row;
	public GraphicActivity(IActivity activity) {
		this.activity=activity;
	}
	@Override
	public int startable() {
		int max=0;
		for(IGraphicActivity act:getPredecessors()){
			if(act.getState()==GraphicActivityState.unfinished){
				return -1;
			}
			if(max<act.getCol()){
				max=act.getCol();
			}
		}
		return max;
	}
	@Override
	public String getUUID() {
		return activity.getUUID();
	}
	@Override
	public String getName() {
		return activity.getName();
	}
	@Override
	public String getDescription() {
		return activity.getDescription();
	}
	@Override
	public void setGUId(String id) {
		activity.setGUId(id);
		
	}
	@Override
	public void setName(String name) {
		activity.setName(name);
		
	}
	@Override
	public void setDescription(String description) {
		activity.setDescription(description);
		
	}
	@Override
	public String getId() {
		return activity.getId();
	}
	@Override
	public void setId(String id) {
		activity.setId(id);
		
	}
	
	
	private List<IGraphicActivity> wrap(List<IActivity> activities){
		List<IGraphicActivity> newActivities=new ArrayList<IGraphicActivity> ();
		for(IActivity activity:activities){
			newActivities.add(new GraphicActivity(activity));
		}
		return newActivities;
	}

	public  Object clone(){
		return null;
	}
	public List<IGraphicActivity> getPredecessors() {
		return predecessors;
	}
	public void setPredecessors(List<IGraphicActivity> predecessors) {
		this.predecessors = predecessors;
	}
	public List<IGraphicActivity> getSuccessors() {
		return successors;
	}
	public void setSuccessors(List<IGraphicActivity> successors) {
		this.successors = successors;
	}
	public GraphicActivityState getState() {
		return state;
	}
	public void setState(GraphicActivityState state) {
		this.state = state;
	}
	@Override
	public void addProperty(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getProperty(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getJobIndex() {
		return Integer.valueOf(((IProcessActivity)activity).getJob().getId());
	}
	

	

}
