package enterprise.materialflow.optimization;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

import java.util.HashMap;
import java.util.Map;

import simulation.model.activity.ActivityState;
import simulation.model.activity.IActivity;
import ui.HashMapR;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.plan.order.job.IJob;
import enterprise.materialflow.model.plan.order.job.JobUtilities;
import enterprise.materialflow.model.plant.resource.IResource;
import enterprise.materialflow.model.product.activity.IProcessActivity;
import enterprise.materialflow.optimization.model.IOptimizationModel;

public class CplexSolver {
	private IloCplex solver;
	private HashMapR<IActivity, IloNumVar> startTimes = new HashMapR<IActivity, IloNumVar>();
	private Table<IActivity, IResource, IloIntVar> allocationVars = HashBasedTable
			.create();
	private Map<IActivity, IloNumExpr> processTimes = new HashMap<IActivity, IloNumExpr>();
	private int relaxVariableNumber=0;

	public CplexSolver(IOptimizationModel model) throws IloException {

		solver = new IloCplex();
		//solver.setParam(IloCplex.LongParam.TimeLimit, 240);
		solver.setParam(IloCplex.IntParam.MIPDisplay, 5);
		generateSolveModel(model);
		System.out.println("Continious Variable Num:" + startTimes.size());
		System.out.println("Binary Variable Num:" + allocationVars.size());
		System.out.println("Relax Variable Num:" + relaxVariableNumber);

	}

	private void generateSolveModel(IOptimizationModel model)
			throws IloException {
		double span = model.getSpan();
		double bigM=span*100;
		// decision variables
		for (IProcessActivity act : model.getActivities()) {
			IloNumVar start = solver.numVar(0, span);
			solver.add(start);
			startTimes.put(act, start);
			for (IResource res : model.getResources()) {
				IloIntVar alloVar = solver.boolVar();
				solver.add(alloVar);
				allocationVars.put(act, res, alloVar);
			}
		}

		// constraints
		for (IProcessActivity act : model.getActivities()) {
			IloNumExpr processTime = solver.constant(0.f);
			for (IResource res : act.getResourceRequirement()
					.getAlternativeResources()) {
				processTime = solver.sum(processTime, solver.prod(act
						.getProcessTime().getProcessTime(res), allocationVars
						.get(act, res)));
			}
			processTimes.put(act, processTime);
			for (IActivity suc : act.getSuccessors()) {
				solver.add(solver.le(
						solver.sum(startTimes.get(act), processTime),
						startTimes.get(suc)));
			}
		}

		for (IResource res : model.getResources()) {
			for (IProcessActivity act1 : model.getActivities()) {
				if (!act1.getResourceRequirement().getAlternativeResources().contains(res)){
					continue;
				}
				IloNumExpr[] sum1 = new IloNumExpr[4];
				sum1[0] = startTimes.get(act1);
				sum1[1] = processTimes.get(act1);
				sum1[2] = solver.prod(-1, solver.constant(bigM));
				sum1[3] = solver.prod(bigM, allocationVars.get(act1, res));

				for (IProcessActivity act2 : model.getActivities()) {
					if (!act2.getResourceRequirement().getAlternativeResources().contains(res)){
						continue;
					}
					if (act1 == act2) {
						continue;
					}
					//IloIntVar relax = solver.boolVar();
					//solver.add(relax);
					relaxVariableNumber++;
					
					IloNumExpr[] sum2 = new IloNumExpr[3];
					sum2[0] = startTimes.get(act1);
					sum2[1] = solver.constant(bigM);
					sum2[2] = solver.prod(-1 * bigM,
							allocationVars.get(act1, res));
					//sum2[3] = solver.prod(span, relax);

					IloNumExpr[] sum3 = new IloNumExpr[4];
					sum3[0] = startTimes.get(act2);
					sum3[1] = processTimes.get(act2);
					sum3[2] = solver.prod(-1, solver.constant(bigM));
					sum3[3] = solver.prod(bigM, allocationVars.get(act2, res));

					IloNumExpr[] sum4 = new IloNumExpr[3];
					sum4[0] = startTimes.get(act2);
					sum4[1] = solver.prod(1, solver.constant(bigM));
					sum4[2] = solver.prod(-1 * bigM,
							allocationVars.get(act2, res));
					//sum4[3] = solver.prod(-1 * span, relax);

					 IloConstraint c1 =
					 solver.le(solver.sum(sum1),solver.sum(sum4));
					 IloConstraint c2 =
					 solver.le(solver.sum(sum3),solver.sum(sum2));
					 solver.add(solver.or(c1,c2));

					//solver.add(solver.le(solver.sum(sum1), solver.sum(sum4)));
					//solver.add(solver.le(solver.sum(sum3), solver.sum(sum2)));

				}
			}
		}

		for (IProcessActivity act : model.getActivities()) {
			IloIntExpr sum1 = solver.constant(0);
			IloIntExpr sum2 = solver.constant(0);
			for (IResource res : model.getResources()) {
				if (act.getResourceRequirement().getAlternativeResources()
						.contains(res)) {
					sum1 = solver.sum(sum1, allocationVars.get(act, res));
				} else {
					//sum2 = solver.sum(sum2, allocationVars.get(act, res));
					solver.add(solver.eq(0, allocationVars.get(act, res)));
				}

			}
			solver.add(solver.eq(1, sum1));
			//solver.add(solver.eq(0, sum2));
		}

		IloNumExpr maxC = solver.constant(0.f);
		for (IActivity act : model.getActivities()) {
			maxC = solver.max(maxC,
					solver.sum(startTimes.get(act), processTimes.get(act)));
		}
		
		if (model.getObjective() == KeyPerformanceEnmu.makespan) {
			// objective minimize make span
			solver.addMinimize(maxC);
		} else if (model.getObjective() == KeyPerformanceEnmu.averageCT) {

			solver.add(solver.le(maxC, 1554));

			// objective minimize average cycle time
			IloNumExpr ctsum = solver.constant(0);
			for (IJob job : model.getJobs()) {
				
				IloNumExpr finishedTime = solver.constant(Integer.MIN_VALUE);
				
				for (IActivity act : JobUtilities.getEndActivities(job)) {
					finishedTime = solver.max(finishedTime, solver.sum(startTimes.get(act),processTimes.get(act)));
				}
				ctsum = solver.sum(ctsum, solver.diff(finishedTime,job.getReleasedTime()));
			}

			IloNumExpr avgCT = solver.prod(ctsum, 1.0/model.getJobs().size());
			solver.addMinimize(avgCT);
		}else{
			System.exit(0);
		}
	}

	public boolean solve() throws IloException {
		if (solver.solve()) {
			getResult();
			return true;
		}
		return false;
	}

	private void getResult() throws UnknownObjectException, IloException {
		for (IActivity act1 : startTimes.keySet()) {
			IProcessActivity act = (IProcessActivity) act1;
			IloNumVar start = startTimes.get(act);
			act.setStartTime((long) solver.getValue(start));
			act.setEndTime((long) (solver.getValue(start) + solver
					.getValue(processTimes.get(act))));
			act.setState(ActivityState.finished);
			for (IResource res : act.getResourceRequirement()
					.getAlternativeResources()) {
				if (Math.abs(solver.getValue(allocationVars.get(act, res)) -1)<1e-5) {
					act.setAssignedResource(res);
				}
			}
		}
	}
	
	public double getObjective(){
		try {
			return solver.getObjValue();
		} catch (IloException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
