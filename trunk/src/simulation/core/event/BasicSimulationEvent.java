package simulation.core.event;


public abstract class BasicSimulationEvent implements ISimulationEvent{
	private long time;
	private SimulationEventType type=SimulationEventType.normal;
	private int priority=1;
	
	public void setType(SimulationEventType type) {
		this.type = type;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(ISimulationEvent o) {
		if(time<o.getTime()){
			return -1;
		}else if(time>o.getTime()){
			return 1;
		}else {
			return 0;
		}
	}

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public SimulationEventType getType() {
		return type;
	}
	
	public void clone(BasicSimulationEvent event){
		event.setTime(time);
		event.setType(type);
	}
	
	public ISimulationEvent clone(){
		return null;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	


}
