package enterprise.materialflow.simulation.event;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.model.plan.order.IManufactureOrder;
import simulation.core.event.BasicSimulationEvent;
import simulation.core.event.ISimulationEvent;
import simulation.core.event.SimulationEventType;

public class OrderReleaseEvent extends BasicSimulationEvent{
	
	private IOrderRelease orderRelease;
	
	public OrderReleaseEvent(IOrderRelease release){
		this.orderRelease=release;
		setType(SimulationEventType.normal);
	}

	@Override
	public List<ISimulationEvent> response(long currentTime) {
		List <ISimulationEvent> events=new ArrayList<ISimulationEvent>();
		List<IManufactureOrder> orders = orderRelease.releaseOrders(currentTime);
		for(IManufactureOrder order:orders){
			JobReleaseEvent event=new JobReleaseEvent(order.getJobRelease());
			event.setTime(currentTime);
			events.add(event);
			
		}
		if(orderRelease.isDone()){
			return events;
		}
		OrderReleaseEvent event=new OrderReleaseEvent(orderRelease);
		event.setTime(orderRelease.nextReleaseTime(currentTime));
		events.add(event);
		return events;
	}

	public IOrderRelease getOrderRelease() {
		return orderRelease;
	}

	public void setOrderRelease(IOrderRelease orderRelease) {
		this.orderRelease = orderRelease;
	}
	
	public OrderReleaseEvent clone(){
		OrderReleaseEvent event=new OrderReleaseEvent(orderRelease);
		super.clone(event);
		return event;
	}

}
