package enterprise.materialflow.optimization;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntervalSequenceVar;
import ilog.concert.IloIntervalVar;
import ilog.concert.IloNumExpr;
import ilog.cp.IloCP;

import java.util.ArrayList;
import java.util.List;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.JobUtilities;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.optimization.model.IOptimizationModel;
import enterprise.materialflow.optimization.model.Mode;
import simulation.model.activity.ActivityState;
import simulation.model.activity.IActivity;
import ui.HashMapR;

public class CPSolver {
	private HashMapR<IActivity, IloIntervalVar> activityVariables = new HashMapR<IActivity, IloIntervalVar>();
	private HashMapR<Mode, IloIntervalVar> modes = new HashMapR<Mode, IloIntervalVar>();
	private HashMapR<IResource, IloIntervalSequenceVar> machineSequenceVariables = new HashMapR<IResource, IloIntervalSequenceVar>();

	private IloCP cp = new IloCP();
	private IOptimizationModel model;

	public CPSolver(IOptimizationModel model) throws IloException {
		this.model = model;
		cp.setParameter(IloCP.IntParam.FailLimit, 40000000);
		cp.setParameter(IloCP.DoubleParam.TimeLimit, 120);
		// cp.setParameter(IloCP.IntParam.LogPeriod, 1);
		//cp.setParameter(IloCP.IntParam.LogVerbosity,
				//IloCP.ParameterValues.Quiet);
		generateCPModel(model);
	}

	@SuppressWarnings("unchecked")
	private void generateCPModel(IOptimizationModel model) throws IloException {
		// decision variables
		for (IActivity act1 : model.getActivities()) {
			IProcessActivity act = (IProcessActivity) act1;
			IloIntervalVar intVar = cp.intervalVar();
			activityVariables.put(act, intVar);
			cp.add(intVar);
			List<Mode> ms = new ArrayList<Mode>();
			for (IResource res : act.getResourceRequirement()
					.getAlternativeResources()) {
				Mode mode = new Mode(act, res, (int) act.getProcessTime()
						.getProcessTime(res));
				ms.add(mode);
				intVar = cp.intervalVar();
				intVar.setSizeMin(mode.getTime());
				intVar.setSizeMax(mode.getTime());
				intVar.setOptional();
				intVar.setLengthMax(mode.getTime());
				intVar.setLengthMin(mode.getTime());
				modes.put(mode, intVar);
				cp.add(intVar);
			}
			act.addProperty("modes", ms);

		}

		for (IResource m : model.getResources()) {
			List<IloIntervalVar> subMode = new ArrayList<IloIntervalVar>();
			for (Mode mode : modes.keySet()) {
				if (mode.getResource() == m) {
					subMode.add(modes.get(mode));
				}
			}
			IloIntervalSequenceVar mch = cp.intervalSequenceVar(subMode
					.toArray(new IloIntervalVar[0]));
			machineSequenceVariables.put(m, mch);
			cp.add(mch);
		}

		IloIntExpr sum = cp.constant(0);
		IloIntExpr maxC = cp.constant(0);
		for (IActivity act : model.getActivities()) {
			maxC = cp.max(maxC, cp.endOf(activityVariables.get(act)));
		}

		if (model.getObjective() == KeyPerformanceEnmu.makespan) {
			// objective minimize make span
			cp.addMinimize(maxC);
		} else if (model.getObjective() == KeyPerformanceEnmu.averageCT) {

			cp.add(cp.le(maxC, 1522));

			// objective minimize average cycle time
			IloIntExpr ctsum = cp.constant(0);
			for (IJob job : model.getJobs()) {
				IloIntExpr finishedTime = cp.constant(Integer.MIN_VALUE);
				for (IActivity act : JobUtilities.getEndActivities(job)) {
					finishedTime = cp.max(finishedTime, cp.endOf(activityVariables.get(act)));
				}
				ctsum = cp.sum(ctsum, cp.diff(finishedTime,(int)job.getReleasedTime()));
			}

			IloIntExpr avgCT = cp.div(ctsum, model.getJobs().size());
			cp.addMinimize(avgCT);
		}else{
			System.exit(0);
		}

		for (IActivity act : model.getActivities()) {
			for (IActivity suc : act.getSuccessors()) {
				cp.add(cp.endBeforeStart(activityVariables.get(act), activityVariables.get(suc)));
			}
			// cp.add(cp.ge(cp.startOf(ops.get(job.getFirstOperation())),job.getReleaseTime()));
			// cp.add(cp.le(cp.endOf(ops.get(job.getLastOperation())),job.getDueDate()));

		}

		for (IActivity act : activityVariables.keySet()) {
			List<Mode> ms = (List<Mode>) act.getProperty("modes");
			IloIntervalVar[] bbb = new IloIntervalVar[ms.size()];
			for (int i = 0; i < bbb.length; i++) {
				bbb[i] = modes.get(ms.get(i));
			}
			cp.add(cp.alternative(activityVariables.get(act), bbb));
		}

		for (IloIntervalSequenceVar mch : machineSequenceVariables.values()) {
			cp.add(cp.noOverlap(mch));
		}

		// continuous constraints on machine
		// IResource cMachine = model.getResources().get(0);
		// IloIntervalSequenceVar mch = mchs.get(cMachine);
		// for (Mode mode : modes.keySet()) {
		// if (mode.getResource() == cMachine) {
		// IloIntervalVar intVar = modes.get(mode);
		// //
		// cp.add(cp.ifThen(cp.presenceOf(intVar),cp.or(cp.eq(cp.startOfNext(mch,
		// // intVar, 0), 0), cp.eq(cp.startOfNext(mch, intVar, 0),
		// // cp.endOf(intVar)))));
		//
		// }
		//
		// }

		// time bounds constraints on operations in jobs
		// for (IActivity activity : model.getActivities()) {
		// for (IActivity suc : activity.getSuccessors()) {
		// // cp.add(cp.le(cp.diff(cp.startOf(ops.get(suc)),
		// // cp.endOf(ops.get(activity))),5));
		// }
		// }

	}

	public boolean solve() throws IloException {
		if(cp.solve()){
		getResult();
		return true;
		}
		return false;
	}

	private void getResult() throws IloException {
		for (IResource res : model.getResources()) {

			IloIntervalSequenceVar seqVar = machineSequenceVariables.get(res);
			if (!cp.isFixed(seqVar)) {
				continue;
			}
			IloIntervalVar intVar;
			try {
				intVar = cp.getFirst(seqVar);
				if(!cp.isFixed(intVar)){
					continue;
				}				
			} catch (IloException e) {
				continue;
			}

			while (true) {				
					Mode mode = modes.getR(intVar);
					mode.getOwner().setState(ActivityState.finished);
					mode.getOwner().setStartTime(cp.getStart(intVar));
					mode.getOwner().setEndTime(cp.getStart(intVar) + mode.getTime());
					((IProcessActivity) mode.getOwner()).setAssignedResource(res);
				
				if (intVar.equals(cp.getLast(seqVar))) {
					break;
				}

				intVar = cp.getNext(seqVar, intVar);
			}
		}
	}

	public double getObjective() {		
		try {
			return cp.getObjValue();
		} catch (IloException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
