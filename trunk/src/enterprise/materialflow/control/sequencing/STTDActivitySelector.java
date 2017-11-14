package enterprise.materialflow.control.sequencing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import common.Entity;
import common.LoggerFormatter;
import enterprise.materialflow.control.BasicSTTDControl;
import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.control.release.IJobRelease;
import enterprise.materialflow.control.release.IOrderRelease;
import enterprise.materialflow.control.routing.IResourceSelector;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.JobListener;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.simulation.EnterpriseSimulation;
import enterprise.materialflow.simulation.IEnterpriseSimulation;
import enterprise.materialflow.simulation.SimEndAfterJobsFinished;
import enterprise.materialflow.simulation.event.ResourceSelectedActivityEvent;
import enterprise.materialflow.simulation.model.EnterpriseSimulationlModelUtilities;
import enterprise.materialflow.simulation.model.IEnterpriseSimulationModel;
import enterprise.materialflow.simulation.result.SimulationResultNew;
import simulation.core.others.ISimulationTerminateCondition;
import simulation.core.others.SimulationDuration;
import simulation.model.activity.IActivity;
import basic.unit.TimeUnitEnum;
import basic.utilities.DeepCopy;
import basic.utilities.Snapshot;
import basic.volume.TimeVolume;

public class STTDActivitySelector extends BasicSTTDControl implements IActivitySelector {
	

	@Override
	public IActivity selectActivity(IResource resource,
			List<IActivity> activities) {

		if (activities.size() == 1) {
			return activities.get(0);
		}

		
		logger.info("======================================================");
		logger.info("Main Simulation is paused at time "
				+ getSimulation().getCurrentTime());
		logger.info("----------------------------------------------------");
		
		List<IJob>jobs=new ArrayList<IJob>();
		for(IActivity act:activities){
			jobs.add(((IProcessActivity)act).getJob());
		}
		
		double bestKpi = -1;
		IActivity bestActivity = null;
		int index = 0;
		SimulationResultNew.save();
		for (IActivity activity : activities) {
			//IEnterpriseSimulation subSimulation=(IEnterpriseSimulation) DeepCopy.copy(simulation);
			IEnterpriseSimulation subSimulation=getSimulation().clone();
			IEnterpriseSimulationModel newModel = subSimulation.getEnterpriseSimulationModel();
			logger.info("Sub Simulation is started at time "
					+ subSimulation.getCurrentTime());
			SimulationResultNew.reset();
			updateToBaseRule(newModel);
			subSimulation.getEndConditions().clear();
		//	((IProcessActivity)activity).getJob().getState()
		//	subSimulation.addEndCondition(new SimulationDuration(new TimeVolume(10,TimeUnitEnum.Day)));
		//	newModel.updateJob();
			//List<IJob> newjobs = EnterpriseSimulationlUtilities.updateJobs(newModel, jobs);
			//ISimulationEndCondition con=new SimEndAfterJobsFinished(newjobs);
			//subSimulation.addEndCondition(con);
			ResourceSelectedActivityEvent event = new ResourceSelectedActivityEvent();
			
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
				bestActivity = activity;
			} else {
				if (KeyPerformanceEnmu.isBetter(kpi, bestKpi,
						getObjective())) {
					bestKpi = kpi;
					bestActivity = activity;
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

}
