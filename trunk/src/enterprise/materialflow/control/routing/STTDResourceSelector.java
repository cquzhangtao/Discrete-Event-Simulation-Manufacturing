package enterprise.materialflow.control.routing;

import java.util.ArrayList;
import java.util.List;

import common.Entity;
import enterprise.materialflow.control.BasicSTTDControl;
import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.event.ActivitySelectedResourceEvent;
import enterprise.materialflow.simulation.event.ResourceSelectedActivityEvent;
import enterprise.materialflow.simulation.model.EnterpriseSimulationlModelUtilities;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import basic.unit.TimeUnitEnum;
import basic.volume.TimeVolume;
import simulation.core.others.SimulationDuration;
import simulation.model.activity.IActivity;

public class STTDResourceSelector extends BasicSTTDControl implements IResourceSelector{

	@Override
	public IResource selectResource(IActivity activity,
			List<IResource> resources) {
		
		if (resources.size() == 1) {
			return resources.get(0);
		}

		
		logger.info("======================================================");
		logger.info("Main Simulation is paused at time "
				+ getSimulation().getCurrentTime());
		logger.info("----------------------------------------------------");
		
		
		double bestKpi = -1;
		IResource bestActivity = null;
		int index = 0;
		SimulationResultNew.save();
		for (IResource resource : resources) {
			//IEnterpriseSimulation subSimulation=(IEnterpriseSimulation) DeepCopy.copy(simulation);
			IEnterpriseSimulation subSimulation=getSimulation().clone();
			IEnterpriseSimulationModel newModel = subSimulation.getEnterpriseSimulationModel();
			logger.info("Sub Simulation is started at time "
					+ subSimulation.getCurrentTime());
			SimulationResultNew.reset();
			updateToBaseRule(newModel);
			subSimulation.getEndConditions().clear();
		//	((IProcessActivity)activity).getJob().getState()
		//subSimulation.addEndCondition(new SimulationDuration(new TimeVolume(10,TimeUnitEnum.Day)));
			//newModel.updateJob();
			//List<IJob> newjobs = EnterpriseSimulationlUtilities.updateJobs(newModel, jobs);
			//ISimulationEndCondition con=new SimEndAfterJobsFinished(newjobs);
			//subSimulation.addEndCondition(con);
			ActivitySelectedResourceEvent event = new ActivitySelectedResourceEvent();
			
			event.setActivity(newModel.getActivities()
					.get(activity.getName()));
			event.setResource(newModel.getResources()
					.get(resource.getName()));
			event.setPriority(0);
			event.setTime(subSimulation.getCurrentTime());
			subSimulation.addEvent(event);
			subSimulation.run();
			SimulationResultNew.stat();
			double kpi = SimulationResultNew
					.getKPI(getObjective());
			if (index == 0) {
				bestKpi = kpi;
				bestActivity = resource;
			} else {
				if (KeyPerformanceEnmu.isBetter(kpi, bestKpi,
						getObjective())) {
					bestKpi = kpi;
					bestActivity = resource;
				}
			}
			index++;
			//logger.info(SimulationResultNew.getProductResultString());
			logger.info("Sub Simulation is done at time "
					+ subSimulation.getCurrentTime());
			logger.info("----------------------------------------------------");
		}
		SimulationResultNew.recover();
		logger.info("Main Simulation continues at time "
				+ getSimulation().getCurrentTime());
		logger.info("======================================================");
		return bestActivity;
	}

	@Override
	public boolean isSelectable(IActivity activity, List<IResource> resources) {
		return true;
	}


}
