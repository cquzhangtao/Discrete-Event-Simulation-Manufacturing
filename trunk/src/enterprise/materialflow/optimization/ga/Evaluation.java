package enterprise.materialflow.optimization.ga;

import java.util.List;

import enterprise.materialflow.optimization.model.IOptimizationModel;
import enterprise.materialflow.optimization.model.OptimizationType;


public class Evaluation {
	
	public static void evaluate(IOptimizationModel model, Individual ind){
		ind.decode();
		if(SchedulingUtilities.addDisjunctiveArcs(ind.getActivities())){
			Network.forward(model.getActivities());
			SchedulingUtilities.removeDisjunctiveArcs(ind.getActivities());
			ind.setFitness(model.getObjectiveValue());
		}else{
			if(model.getOptimizationType()==OptimizationType.min){
				ind.setFitness(Double.MAX_VALUE/10000);
			}else{
				ind.setFitness(Double.MIN_VALUE);
			}
		}
		SchedulingUtilities.removeDisjunctiveArcs(ind.getActivities());

		
		
	}
	
	public static void evaluate(IOptimizationModel model,List<Individual> inds){
		for(Individual ind:inds){
			evaluate(model,ind);
		}
	}
	
	

}
