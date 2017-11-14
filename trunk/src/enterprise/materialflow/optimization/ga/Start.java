package enterprise.materialflow.optimization.ga;


import start.GanntUI;
import enterprise.materialflow.control.KeyPerformanceEnmu;
import enterprise.materialflow.model.IEnterpriseModel;
import enterprise.materialflow.model.test.ChainModel;
import enterprise.materialflow.optimization.CPSolver;
import enterprise.materialflow.optimization.model.OptimizationModel;
import enterprise.materialflow.optimization.model.OptimizationType;

public class Start {

	public static void main(String[] args) {
		IEnterpriseModel enterprise = ChainModel.finiteModel();
		OptimizationModel model = new OptimizationModel(enterprise);
		model.setObjective(KeyPerformanceEnmu.averageCT);
		model.setOptimizationType(OptimizationType.min);
		GeneticAlgorithm solver=new GeneticAlgorithm();
		solver.setPopulationSize(20);
		solver.setGenerationNum(1000);
//		Individual ind = PopulationUtilities.generateIndividual(model);
//		SchedulingUtilities.addDisjunctiveArcs(ind.getActivities());
//		Network.forward(model.getActivities());
//		SchedulingUtilities.removeDisjunctiveArcs(ind.getActivities());
		Population initPop = PopulationUtilities.generateRandomPopulation(model,solver.getPopulationSize());
		solver.setPopulation(initPop);
		solver.run();
		System.out.println(model.getMakespan());
		System.out.println(model.getAverageCycleTime());
		GanntUI ui = new GanntUI(model);
		ui.pack();
		ui.setVisible(true);
		


	}

}
