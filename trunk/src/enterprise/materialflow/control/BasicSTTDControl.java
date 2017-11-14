package enterprise.materialflow.control;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import common.Entity;
import common.LoggerFormatter;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.control.routing.IResourceSelector;
import enterprise.materialflow.control.sequencing.IActivitySelector;
import enterprise.materialflow.control.sequencing.STTDActivitySelector;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.IProduct;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import simulation.model.activity.IActivity;

public class BasicSTTDControl extends Entity {

	protected final static Logger logger = Logger
			.getLogger(BasicSTTDControl.class.getName());
	private IEnterpriseSimulation simulation;
	private IActivitySelector baseActivitySelector;
	private IResourceSelector baseResourceSelector;
	private IJobRelease baseJobRelease;
	private IOrderRelease baseOrderRelease;
	private KeyPerformanceEnmu objective;

	public BasicSTTDControl() {
		for (Handler handler : logger.getHandlers()) {
			if (handler.getClass() == ConsoleHandler.class)
				logger.removeHandler(handler);
		}
		logger.setUseParentHandlers(false);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new LoggerFormatter());
		logger.addHandler(handler);

	}

	public IEnterpriseSimulation getSimulation() {
		return simulation;
	}

	public void setSimulation(IEnterpriseSimulation simulation) {
		this.simulation = simulation;
	}

	public IActivitySelector getBaseActivitySelector() {
		return baseActivitySelector;
	}

	public void setBaseActivitySelector(IActivitySelector baseActivitySelector) {
		this.baseActivitySelector = baseActivitySelector;
	}

	public IResourceSelector getBaseResourceSelector() {
		return baseResourceSelector;
	}

	public void setBaseResourceSelector(IResourceSelector baseResourceSelector) {
		this.baseResourceSelector = baseResourceSelector;
	}

	public IJobRelease getBaseJobRelease() {
		return baseJobRelease;
	}

	public void setBaseJobRelease(IJobRelease baseJobRelease) {
		this.baseJobRelease = baseJobRelease;
	}

	public IOrderRelease getBaseOrderRelease() {
		return baseOrderRelease;
	}

	public void setBaseOrderRelease(IOrderRelease baseOrderRelease) {
		this.baseOrderRelease = baseOrderRelease;
	}

	protected void updateToBaseRule(IEnterpriseSimulationModel newModel) {
		newModel.updateActivity();
		for (IActivity activity : newModel.getActivities().values()) {
			if (activity instanceof IProcessActivity) {
				((IProcessActivity) activity)
						.setResourceSelector(baseResourceSelector);
			}
		}
		for (IResource resource : newModel.getResources().values()) {
			resource.setActivitySelector(baseActivitySelector);
		}
		for(IProduct product:newModel.getProducts().values()){
			for(IActivity activity:product.getActivities().values()){
				((IProcessActivity) activity)
				.setResourceSelector(baseResourceSelector);
			}
		}
		// for(IManufactureOrder
		// order:newModel.getMetaModel().getOrders().values()){
		// order.setJobRelease(baseJobRelease);
		// }
		// newModel.getProductionPlan().setOrderRelease(baseOrderRelease);
	}

	public KeyPerformanceEnmu getObjective() {
		return objective;
	}

	public void setObjective(KeyPerformanceEnmu objective) {
		this.objective = objective;
	}

}
